package com.auth.jwt.security;

import com.auth.jwt.dto.RequestDto;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties(prefix = "admin-paths")
public class AdminRouteValidator {
    @Getter
    private List<RequestDto> paths;

    public boolean isAdmin(RequestDto reqDto) {
        return paths.stream().anyMatch(
                p -> Pattern.matches(p.getUri(),reqDto.getUri()) && p.getMethod().equals(reqDto.getMethod())
        );
    }
}
