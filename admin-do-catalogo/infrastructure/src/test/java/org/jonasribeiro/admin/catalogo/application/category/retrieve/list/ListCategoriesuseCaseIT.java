package org.jonasribeiro.admin.catalogo.application.category.retrieve.list;

import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategorySearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesuseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        final var categories = Stream.of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("NEtflix Originals", "Títulos de autoria da Netflix", true),
                        Category.newCategory("Amazon Originals", "Título de autoria da Amazon", true),
                        Category.newCategory("Disney", "Título de autoria da Disney", true),
                        Category.newCategory("HBO", "Título de autoria da HBO", true),
                        Category.newCategory("Apple TV", "Título de autoria da Apple TV", true),
                        Category.newCategory("Paramount", "Título de autoria da Paramount", true),
                        Category.newCategory("Sony", "Título de autoria da Sony", true),
                        Category.newCategory("Warner", "Título de autoria da Warner", true)
                )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenValidTerm_whenTermDoesntMatchPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "eaewe ewgwgwgg";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualOutput = useCase.execute(aQuery);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
    }

   @ParameterizedTest
   @CsvSource({

        "fil,0,10,1,1,Filmes",
        "Net,0,10,1,1,NEtflix Originals",
        "ZON,0,10,1,1,Amazon Originals",
        "War,0,10,1,1,Warner",
        "TV,0,10,1,1,Apple TV",
        "pa,0,10,1,1,Paramount",

   })
    public void givenValidTerm_whenCallListCategories_ShouldReturnCategoriesFiltered(
           final String expectedTerms,
           final int expectedPage,
           final int expectedPerPage,
           final int expectedItemsCount,
           final int expectedTotal,
           final String expectedCategoryName
   ) {
           final var expectedDirection = "asc";
           final var expectedSort = "name";

       final var aQuery = new CategorySearchQuery(
               expectedPage,
               expectedPerPage,
               expectedTerms,
               expectedSort,
               expectedDirection
       );

       final var actualOutput = useCase.execute(aQuery);

       Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
       Assertions.assertEquals(expectedPage, actualOutput.currentPage());
       Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
       Assertions.assertEquals(expectedTotal, actualOutput.total());
       Assertions.assertEquals(expectedCategoryName, actualOutput.items().get(0).name());
   }

   @ParameterizedTest
   @CsvSource({
              "name,asc,0,10,9,9,Amazon Originals",
              "name,desc,0,10,9,9,Warner",

   })
   public void givenAValidSortAndDirection_whenCallListCategories_thenShouldReturnCategoriesOrdered(
           final String expectedSort,
           final String expectedDirection,
           final int expectedPage,
           final int expectedPerPage,
           final int expectedItemsCount,
           final long expectedTotal,
           final String expectedCategoryName
   ) {
         final var expectedTerms = "";

         final var aQuery = new CategorySearchQuery(
               expectedPage,
               expectedPerPage,
               expectedTerms,
               expectedSort,
               expectedDirection
       );

       final var actualOutput = useCase.execute(aQuery);

       Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
       Assertions.assertEquals(expectedPage, actualOutput.currentPage());
       Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
       Assertions.assertEquals(expectedTotal, actualOutput.total());
       Assertions.assertEquals(expectedCategoryName, actualOutput.items().get(0).name());

   }

   @ParameterizedTest
   @CsvSource({

        "0,2,2,9,Amazon Originals;Apple TV",
        "1,2,2,9,Disney;Filmes",
        "2,2,2,9,HBO;NEtflix Originals",
        "3,2,2,9,Paramount;Sony",
        "4,2,1,9,Warner",

   })
   public void givenValidPage_whenCallsListCategories_shouldReturnCategoriesPagineted(
           final int expectedPage,
           final int expectedPerPage,
           final int expectedItemsCount,
           final long expectedTotal,
           final String expectedCategoriesName
   ) {
       final var expectedTerms = "";
       final var expectedSort = "name";
       final var expectedDirection = "asc";

       final var aQuery = new CategorySearchQuery(
               expectedPage,
               expectedPerPage,
               expectedTerms,
               expectedSort,
               expectedDirection
       );

       final var actualOutput = useCase.execute(aQuery);

       Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
       Assertions.assertEquals(expectedPage, actualOutput.currentPage());
       Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
       Assertions.assertEquals(expectedTotal, actualOutput.total());

       int index = 0;
       for (final String expectedName : expectedCategoriesName.split(";")) {
           final String actualName = actualOutput.items().get(index).name();
              Assertions.assertEquals(expectedName, actualName);
              index++;
       }
   }
}
