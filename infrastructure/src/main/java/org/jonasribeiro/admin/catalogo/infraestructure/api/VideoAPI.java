package org.jonasribeiro.admin.catalogo.infraestructure.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.infraestructure.video.models.CreateVideoRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.video.models.VideoListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.video.models.VideoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RequestMapping(value = "videos")
@Tag(name = "video")
public interface VideoAPI {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List videos paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of videos paginated"),
            @ApiResponse(responseCode = "422", description = "A query param is not valid"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    Pagination<VideoListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "25") int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "title") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "cast_members_ids", required = false, defaultValue = "") Set<String> castMembers,
            @RequestParam(name = "categories_ids", required = false, defaultValue = "") Set<String> categories,
            @RequestParam(name = "genres_ids", required = false, defaultValue = "") Set<String> genres
    );

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video with all medias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    ResponseEntity<?> createFull(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "year_launched", required = false) Integer yearLaunched,
            @RequestParam(name = "duration", required = false) Double duration,
            @RequestParam(name = "opened", required = false) Boolean opened,
            @RequestParam(name = "published", required = false) Boolean published,
            @RequestParam(name = "rating", required = false) String rating,
            @RequestParam(name = "categories_id", required = false) Set<String> categories,
            @RequestParam(name = "cast_members_id", required = false) Set<String> castMembers,
            @RequestParam(name = "genres_id", required = false) Set<String> genres,
            @RequestParam(name = "video_file", required = false) MultipartFile videoFile,
            @RequestParam(name = "trailer_file", required = false) MultipartFile trailerFile,
            @RequestParam(name = "banner_file", required = false) MultipartFile bannerFile,
            @RequestParam(name = "thumb_file", required = false) MultipartFile thumbFile,
            @RequestParam(name = "thumb_half_file", required = false) MultipartFile thumbHalfFile
    );

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video with all medias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    ResponseEntity<?> createPartial(@RequestBody CreateVideoRequest payload);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a video by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video with given id was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    VideoResponse getById(@PathVariable(name = "id") String anId);

@PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video with all medias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video updated successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    ResponseEntity<?> update(@PathVariable (name = "id") String anId, @RequestBody CreateVideoRequest payload);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a video by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vídeo deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    void deleteById(@PathVariable(name = "id") String id);

    @GetMapping(value = "{id}/medias/{type}")
    @Operation(summary = "Get a video media by it's type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Media was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    ResponseEntity<byte[]> getMediaVideoByType(
            @PathVariable(name = "id") String anId,
            @PathVariable(name = "type") String type
    );

    @PostMapping(value = "{id}/medias/{type}")
    @Operation(summary = "Upload a video media by it's type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Media retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Media was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later"),
    })
    ResponseEntity<?> uploadMediaVideoByType(
            @PathVariable(name = "id") String anId,
            @PathVariable(name = "type") String type,
            @RequestParam("media_file") MultipartFile mediaFile
    );

}
