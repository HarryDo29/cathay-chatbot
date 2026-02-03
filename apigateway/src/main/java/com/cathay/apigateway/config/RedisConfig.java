//package com.cathay.apigateway.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//    @Bean
//    @Primary
//    public ReactiveRedisTemplate<String, Object> redisTemplate (ReactiveRedisConnectionFactory factory){
//        StringRedisSerializer keySerializer = new StringRedisSerializer(); // Key serializer as String type
//        ObjectMapper objectMapper = new ObjectMapper(); // tạo 1 ObjectMapper để cấu hình JSON serialization
//        objectMapper.registerModule(new JavaTimeModule()); // Support for Java 8 date/time types convert LocalDateTime --> String
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        Jackson2JsonRedisSerializer<Object> valueSerializer =
//                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class); // Value serializer as JSON type
//        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder =
//                RedisSerializationContext.newSerializationContext(keySerializer); // Build context with key serializer
//        RedisSerializationContext<String, Object> context = builder
//                .value(valueSerializer)
//                .hashKey(keySerializer)
//                .build(); // Complete context with value and hash key serializers
//        return new ReactiveRedisTemplate<>(factory, context);
//    }
//}
