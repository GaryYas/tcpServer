package server.tcp.db.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jary on 7/27/2018.
 */
@Document(collection = "keyValePairs")
@Data
@Builder
public class KeyData {
    @Id
    String key;
    List<String> values;


}
