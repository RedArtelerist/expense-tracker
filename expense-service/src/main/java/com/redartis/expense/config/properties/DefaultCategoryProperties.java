package com.redartis.expense.config.properties;

import com.redartis.dto.constants.Type;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default-category")
@Getter
@Setter
public class DefaultCategoryProperties {
    private List<DefaultCategory> categories;

    @Getter
    @Setter
    public static class DefaultCategory {
        private String name;
        private Type type;
    }
}
