package at.backend.MarketingCompany.crm.quote.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.quote.domain.Quote;
import at.backend.MarketingCompany.crm.quote.domain.QuoteItem;
import at.backend.MarketingCompany.crm.quote.infrastructure.DTOs.QuoteInput;
import at.backend.MarketingCompany.crm.quote.infrastructure.DTOs.QuoteItemInput;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:36:05-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class QuoteMappersImpl implements QuoteMappers {

    @Override
    public Quote inputToEntity(QuoteInput input) {
        if ( input == null ) {
            return null;
        }

        Quote quote = new Quote();

        quote.setValidUntil( input.validUntil() );
        quote.setStatus( input.status() );
        quote.setItems( quoteItemInputListToQuoteItemList( input.items() ) );

        return quote;
    }

    @Override
    public Quote inputToUpdatedEntity(Quote existingQuote, QuoteInput input) {
        if ( input == null ) {
            return existingQuote;
        }

        existingQuote.setValidUntil( input.validUntil() );
        existingQuote.setStatus( input.status() );
        if ( existingQuote.getItems() != null ) {
            List<QuoteItem> list = quoteItemInputListToQuoteItemList( input.items() );
            if ( list != null ) {
                existingQuote.getItems().clear();
                existingQuote.getItems().addAll( list );
            }
            else {
                existingQuote.setItems( null );
            }
        }
        else {
            List<QuoteItem> list = quoteItemInputListToQuoteItemList( input.items() );
            if ( list != null ) {
                existingQuote.setItems( list );
            }
        }

        return existingQuote;
    }

    protected QuoteItem quoteItemInputToQuoteItem(QuoteItemInput quoteItemInput) {
        if ( quoteItemInput == null ) {
            return null;
        }

        QuoteItem quoteItem = new QuoteItem();

        quoteItem.setDiscountPercentage( quoteItemInput.discountPercentage() );

        return quoteItem;
    }

    protected List<QuoteItem> quoteItemInputListToQuoteItemList(List<QuoteItemInput> list) {
        if ( list == null ) {
            return null;
        }

        List<QuoteItem> list1 = new ArrayList<QuoteItem>( list.size() );
        for ( QuoteItemInput quoteItemInput : list ) {
            list1.add( quoteItemInputToQuoteItem( quoteItemInput ) );
        }

        return list1;
    }
}
