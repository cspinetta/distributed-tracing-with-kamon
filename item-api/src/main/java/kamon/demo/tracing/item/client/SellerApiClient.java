package kamon.demo.tracing.item.client;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.typesafe.config.Config;
import java.util.Optional;
import kamon.demo.tracing.item.model.Seller;
import kamon.demo.tracing.item.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
public class SellerApiClient implements SellerRepository {

    RestTemplate restTemplate;
    Config config;

    @Autowired
    public SellerApiClient(RestTemplate restTemplate, Config config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public Optional<Seller> findById(Long id) {
        final String url = format("%s/api/seller/%d", config.getString("service.seller-api.host"), id);
        try {
            return Optional.of(restTemplate.getForObject(url, Seller.class));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != NOT_FOUND) {
                throw ex;
            } else {
                return Optional.empty();
            }
        }
    }

}
