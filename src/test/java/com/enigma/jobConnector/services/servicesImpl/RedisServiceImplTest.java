package com.enigma.jobConnector.services.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedisServiceImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private RedisServiceImpl redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnNothingWhenSave() {
        String key = "key";
        String value = "value";
        Duration duration = Duration.ofHours(1);


        ValueOperations<String, String> operations = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(operations);

        Mockito.doNothing().when(operations).set(key, value, duration);

        redisService.save(key, value, duration);

        Mockito.verify(
                redisTemplate,
                Mockito.times(1)
        ).opsForValue();
        Mockito.verify(
                operations,
                Mockito.times(1)
        ).set(key, value, duration);
    }

    @Test
    void shouldReturnStringWhenGet() {
        String mockKey = "key";
        String mockValue = "value";

        ValueOperations<String, String> operations = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(operations);

        Mockito.when(operations.get(mockKey)).thenReturn(mockValue);

        String value = redisService.get(mockKey);

        Mockito.verify(
                redisTemplate,
                Mockito.times(1)
        ).opsForValue();
        Mockito.verify(
                operations,
                Mockito.times(1)
        ).get(mockKey);
        assertEquals(mockValue, value);
    }

    @Test
    void shouldReturnNothingWhenDelete() {
        String mockKey = "key";

        Mockito.when(redisTemplate.delete(mockKey)).thenReturn(true);

        redisService.delete(mockKey);

        Mockito.verify(
                redisTemplate,
                Mockito.times(1)
        ).delete(mockKey);
    }

    @Test
    void shouldReturnTrueWhenIsExist() {
        String mockKey = "key";

        Mockito.when(redisTemplate.hasKey(mockKey)).thenReturn(true);

        Boolean keyIsExist = redisService.isExist(mockKey);

        Mockito.verify(
                redisTemplate,
                Mockito.times(1)
        ).hasKey(mockKey);
        assertEquals(true, keyIsExist);
    }

    @Test
    void shouldReturnFalseWhenIsExist() {
        String mockKey = "key";

        Mockito.when(redisTemplate.hasKey(mockKey)).thenReturn(false);

        Boolean keyIsNotExist = redisService.isExist(mockKey);

        Mockito.verify(
                redisTemplate,
                Mockito.times(1)
        ).hasKey(mockKey);
        assertEquals(false, keyIsNotExist);
    }
}