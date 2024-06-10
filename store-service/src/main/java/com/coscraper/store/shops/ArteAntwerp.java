package com.coscraper.store.shops;

import com.coscraper.store.enums.ProductScrapingStatus;
import com.coscraper.store.models.shop.ShopResult;
import com.coscraper.store.models.product.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ArteAntwerp implements Shop {
    private static final Logger log = LoggerFactory.getLogger(ArteAntwerp.class);
    private static final String SEARCH_PARAMS = "/search?q=";

    private final UUID id;
    private final String baseUrl;

    public ArteAntwerp(UUID id, String baseUrl) {
        this.id = id;
        this.baseUrl = baseUrl;
    }

    @Override
    public Future<ShopResult> findProductByQuery(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String productQueryUrl = createProductQueryUrl(query);
                String responseContents = fetchResponseContents(productQueryUrl);

                List<String> productUrls = extractProductUrls(responseContents);
                if (productUrls.isEmpty()) {
                    return new ShopResult(null, ProductScrapingStatus.PRODUCT_NOT_FOUND);
                }

                // Always return the first product
                String productUrl = productUrls.get(0);
                Product product = scrapeProductFromUrl(productUrl).get();
                return new ShopResult(Optional.ofNullable(product), ProductScrapingStatus.PRODUCT_FOUND);
            } catch (IOException | InterruptedException e) {
                log.error("Error fetching response contents: {}", e.getMessage());
                return new ShopResult(Optional.empty(), ProductScrapingStatus.STORE_ERROR);
            } catch (Exception e) {
                log.error("Error occurred: {}", e.getMessage());
                return new ShopResult(Optional.empty(), ProductScrapingStatus.STORE_ERROR);
            }
        });
    }

    @Override
    public Future<Product> scrapeProductFromUrl(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document document = Jsoup.connect(url).get();
                return extractProductDetails(document, url);
            } catch (IOException e) {
                log.error("Error occurred while scraping product: {}", e.getMessage());
                throw new RuntimeException("Error scraping product", e);
            }
        });
    }

    private String createProductQueryUrl(String query) {
        return baseUrl + SEARCH_PARAMS + query;
    }

    private String fetchResponseContents(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.html();
    }

    private List<String> extractProductUrls(String responseContents) throws IOException {
        List<String> productUrls = new ArrayList<>();
        Document document = Jsoup.parse(responseContents);
        Elements productElements = document.select(".product-link"); // Adjust the selector based on actual HTML

        for (Element element : productElements) {
            String url = element.attr("href");
            if (!url.startsWith("http")) {
                url = baseUrl + url;
            }
            productUrls.add(url);
        }

        return productUrls;
    }

    private Product extractProductDetails(Document document, String url) {
        UUID productId = UUID.randomUUID();

        // Check for JSON-LD script tag
        Element jsonLdElement = document.select("script[type=application/ld+json]:nth-of-type(3)").first();
        if (jsonLdElement != null) {
            try {
                String json = jsonLdElement.html();
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getString("@type").equals("Product")) {
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String color = document.select("div.color_selector h3").text().replace("Color: ", "");
                    String imageUrl = jsonObject.getJSONArray("image").getString(0);
                    double price = jsonObject.getJSONArray("offers").getJSONObject(0).getDouble("price");

                    return new Product(productId, this.id, name, description, color, url, price, price, imageUrl);
                }
            } catch (JSONException e) {
                log.error("Error parsing JSON-LD data: {}", e.getMessage());
            }
        }

        // Fallback to HTML scraping if JSON-LD is not found
        String name = document.select("h1.product-single__title").text(); // Updated selector for the name
        String description = document.select("div.product-single__description").text(); // Updated selector for description
        String sku = "";
        String priceText = document.select("span.price-item").text().replace("€", "").replace(",", "").trim();
        double price = 0.0;
        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                log.error("Error parsing price: {}", e.getMessage());
            }
        }
        String imageUrl = document.select("img.product__media-image").attr("src");

        if (!imageUrl.startsWith("http")) {
            imageUrl = baseUrl + imageUrl;
        }

        return new Product(productId, this.id, name, description, sku, url, price, price, imageUrl);
    }
}