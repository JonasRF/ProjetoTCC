package org.jonasribeiro.admin.catalogo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {
    @Test
    public void testNewUseCase() {

        UseCase<String, String> useCase = new UseCase<>() {
            @Override
            public String execute(String anIn) {
                return "Hello, " + anIn + "!";
            }
        };

        String result = useCase.execute("World");
        Assertions.assertEquals("Hello, World!", result);

    }
}
