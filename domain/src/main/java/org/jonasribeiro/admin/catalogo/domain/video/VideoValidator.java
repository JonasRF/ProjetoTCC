package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4_000;

    private final Video video;

    public VideoValidator(final Video video, final ValidationHandler handler ) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints() {
        if (this.video.getTitle() == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
        } else if (this.video.getTitle().isEmpty()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
        } else if (this.video.getTitle().length() > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
        }
    }

    private void checkDescriptionConstraints() {
        if (this.video.getDescription() == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
        } else if (this.video.getDescription().isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
        }
        else if (this.video.getDescription().length() > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error("'description' must be between 1 and 4000 characters"));
        }
    }

    private void checkLaunchedAtConstraints() {
        if (this.video.getLaunchedAt() == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraints() {
        if (this.video.getRating() == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
