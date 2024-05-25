package com.coscraper.store.shops;

import com.coscraper.store.enums.ProductScrapingStatus;
import com.coscraper.store.models.shop.ShopResult;
import com.coscraper.store.models.product.Product;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArteAntwerp implements Shop {
    private static final Logger log = LoggerFactory.getLogger(ArteAntwerp.class);
    private static final String SEARCH_PARAMS = "/search?q=";

    private final UUID id;
    private final String baseUrl;

    public ArteAntwerp(UUID id, String name, String baseUrl) {
        this.id = id;
        this.baseUrl = baseUrl;
    }

    @Override
    public Future<ShopResult> findProductByQuery(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String productQueryUrl = createProductQueryUrl(query);
                String responseContents = fetchResponseContents(productQueryUrl);

                ArteAntwerpResponse arteAntwerpResponse = parseResponse(responseContents);
                if (arteAntwerpResponse == null) {
                    return new ShopResult(null, ProductScrapingStatus.STORE_ERROR);
                }

                List<Product> products = extractProduct(arteAntwerpResponse);
                if (products.isEmpty()) {
                    return new ShopResult(null, ProductScrapingStatus.PRODUCT_NOT_FOUND);
                }

                // Always return the first product
                Product product = products.get(0);
                return new ShopResult(Optional.ofNullable(product), ProductScrapingStatus.PRODUCT_FOUND);
            } catch (IOException e) {
                log.error("Error fetching response contents: {}", e.getMessage());
                return new ShopResult(Optional.empty(), ProductScrapingStatus.STORE_ERROR);
            } catch (JSONException e) {
                log.error("Error parsing JSON response: {}", e.getMessage());
                return new ShopResult(Optional.empty(), ProductScrapingStatus.STORE_ERROR);
            }
        });
    }

    @Override
    public Future<Product> scrapeProductFromUrl(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String responseContents = fetchResponseContents(url);
                ArteAntwerpResponse arteAntwerpResponse = parseResponse(responseContents);
                if (arteAntwerpResponse == null) {
                    log.error("Error fetching response contents from URL: {}", url);
                }

                List<Product> productVariants = extractProduct(arteAntwerpResponse);
                if (productVariants.isEmpty()) {
                    log.error("No product found for url: {}", url);
                    return null;
                } else {
                    // Always return the first
                    return productVariants.get(0);
                }
            } catch (IOException | JSONException e) {
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

    private ArteAntwerpResponse parseResponse(String responseContents) throws JSONException {
        Document document = Jsoup.parse(responseContents);
        Element scriptElement = document.getElementById("web-pixels-manager-setup");
        if (scriptElement == null) return null;

        String html = scriptElement.html();
        Matcher matcher = Pattern.compile("search_submitted\", (.+?)\\);").matcher(html);
        if (!matcher.find()) return null;

        String json = matcher.group(1).replace("'", "\"");
        return new ArteAntwerpResponse(json);
    }

    private List<Product> extractProduct(ArteAntwerpResponse arteAntwerpResponse) throws JSONException {
        List<Product> products = new ArrayList<>();
        JSONArray productVariants = arteAntwerpResponse.getSearchResult().getJSONArray("productVariants");

        for (int i = 0; i < productVariants.length(); i++) {
            JSONObject variant = productVariants.getJSONObject(i);
            JSONObject product = variant.getJSONObject("product");
            JSONObject image = variant.getJSONObject("image");

            UUID productId = UUID.randomUUID();
            String name = product.getString("title");
            String sku = variant.getString("sku");
            String url = baseUrl + product.getString("url");
            Double price = variant.getJSONObject("price").getDouble("amount");
            String imageUrl = "https:" + image.getString("src");

            Product prod = new Product(productId, this.id, name, sku, url, price, price, imageUrl);
            products.add(prod);

            log.info("Found product: {}", prod);
        }

        return products;
    }

    @Getter
    private static class ArteAntwerpResponse {
        private final JSONObject searchResult;

        public ArteAntwerpResponse(String json) throws JSONException {
            this.searchResult = new JSONObject(json).getJSONObject("searchResult");
        }
    }
}
