package org.jonasribeiro.admin.catalogo.application.video.delete;

import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.exceptions.InternalErrorException;
import org.jonasribeiro.admin.catalogo.domain.video.MediaResourceGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(videoGateway).deleteById(any());

        doNothing().when(mediaResourceGateway).clearResources(any());

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        // then
         verify(videoGateway).deleteById(eq(expectedId));
            verify(mediaResourceGateway).clearResources(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedId = VideoID.from("1231");

        doNothing().when(videoGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        // then
        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var expectedId = VideoID.from("1231");
        final var expectedErrorMessage = "Gateway error";

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(videoGateway).deleteById(any());

        // when
        final var actualException = Assertions.assertThrows(
                 InternalErrorException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        // then
        verify(videoGateway).deleteById(eq(expectedId));
    }
}
