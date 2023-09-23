package com.example.velocity.controller;

import com.example.velocity.model.Country;
import com.example.velocity.model.Field;
import com.example.velocity.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class VelocityWriterController {

    static String modelName = "User";
    static String packageName = "com.example.velocity";

    @GetMapping("/get/class")
    public String displayClass() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("src/main/resources/templates/class.vm");
        VelocityContext context = new VelocityContext();

        if (packageName != null) {
            context.put("packagename", packageName);
        }

        List<Field> properties = new ArrayList<>();
        properties.add(new Field("id", "int"));
        properties.add(new Field("firstName", "String"));
        properties.add(new Field("lastName", "String"));
        context.put("className", modelName);
        context.put("properties", properties);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    @GetMapping("/get/value")
    public String displayValueCondition() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("src/main/resources/templates/condition.vm");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    @GetMapping("/get/date")
    public String displayDate() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("src/main/resources/templates/date.vm");
        VelocityContext context = new VelocityContext();
        context.put("currentDateAndTime", new Date());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    @GetMapping("/get/products")
    public ResponseEntity<String> getAllProducts() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        // Load the Velocity template
        Template template = velocityEngine.getTemplate("src/main/resources/templates/selectProductListTemplate.vm");

        VelocityContext context = new VelocityContext();

        // Create a list of products (You can replace this with your data source)
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 19.99));
        products.add(new Product(2L, "Product 2", 29.99));
        products.add(new Product(3L, "Product 3", 9.99));
        products.add(new Product(4L, "Product 4", 39.99));

        context.put("products", products);

        // Merge the template with the context and write the result to the StringWriter
        StringWriter writer = new StringWriter();

        template.merge(context, writer);


        // Get the HTML content as a string
        String htmlContent = writer.toString();

        // Create a ResponseEntity with the HTML content and set the content type
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.TEXT_HTML).body(htmlContent);

    }

    @GetMapping("/get/countries")
    public String getAllCountries(){
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init();

            // Load the Velocity template
            Template template = velocityEngine.getTemplate("src/main/resources/templates/country.vm");

            VelocityContext context = new VelocityContext();

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API endpoint
        String apiUrl = "http://localhost:8084/referential/countries";

        // Make an HTTP GET request to the API and get the response as an array of Country objects
        ResponseEntity<Country[]> responseEntity = restTemplate.getForEntity(apiUrl, Country[].class);

        List<Country> countryList = Arrays.stream(responseEntity.getBody())
                .map(data -> new Country(
                        data.getId(),
                        data.getCode(),
                        data.getCode2(),
                        data.getLabel(),
                        data.getNationality()
                )).collect(Collectors.toList());

        // Add the list of labels to the model to make it available in the Velocity template
            context.put("countries", countryList);

            // Merge the template with the context and write the result to the StringWriter
            StringWriter writer = new StringWriter();

            template.merge(context, writer);


            return writer.toString();
        }
    }
