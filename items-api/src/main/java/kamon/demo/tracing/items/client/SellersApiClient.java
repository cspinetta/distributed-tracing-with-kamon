package kamon.demo.tracing.items.client;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.typesafe.config.Config;
import io.vavr.control.Option;
import java.util.Optional;
import kamon.demo.tracing.items.model.Seller;
import kamon.demo.tracing.items.repositories.SellersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
public class SellersApiClient implements SellersRepository {

    private static Logger logger = LoggerFactory.getLogger(SellersApiClient.class);

    RestTemplate restTemplate;
    Config config;

    @Autowired
    public SellersApiClient(RestTemplate restTemplate, Config config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public Optional<Seller> findById(Long id) {
        final String url = format("%s/api/sellers/%d", config.getString("service.sellers-api.host"), id);
        try {
            return Option
                .of(restTemplate.getForObject(url, Seller.class))
                .orElse(() -> {
                    logger.warn("Receive null by Sellers API invoked with " + url);
                    return Option.none();
                })
                .toJavaOptional();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != NOT_FOUND) {
                throw ex;
            } else {
                return Optional.empty();
            }
        }
    }

}
