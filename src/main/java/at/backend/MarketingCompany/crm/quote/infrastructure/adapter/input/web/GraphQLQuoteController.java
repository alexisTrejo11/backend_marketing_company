package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web;

import at.backend.MarketingCompany.crm.quote.api.dto.QuoteOutput;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteQueryPort;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteServicePort;
import at.backend.MarketingCompany.crm.quote.application.port.in.QuoteQueryPort;
import at.backend.MarketingCompany.crm.quote.application.port.in.QuoteServicePort;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteInput;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteItemInput;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteOutput;
import at.backend.MarketingCompany.common.utils.PageInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQLQuoteController {
    
    private final QuoteServicePort quoteServicePort;
    private final QuoteQueryPort quoteQueryPort;
    
    @QueryMapping
    public Page<QuoteOutput> getAllQuotes(@Argument PageInput input) {
        Pageable pageable = PageRequest.of(input.page(), input.size());
        return quoteQueryPort.getAllQuotes(pageable);
    }
    
    @QueryMapping
    public QuoteOutput getQuoteById(@Argument Long id) {
        return quoteQueryPort.getQuoteById(id);
    }
    
    @MutationMapping
    public QuoteOutput createQuote(@Valid @Argument QuoteInput input) {
        return quoteServicePort.createQuote(input);
    }
    
    @MutationMapping
    public QuoteOutput addQuoteItem(@Valid @Argument Long id, @Argument QuoteItemInput input) {
        return quoteServicePort.addQuoteItem(id, input);
    }
    
    @MutationMapping
    public QuoteOutput deleteQuoteItem(@Valid @Argument Long itemId) {
        return quoteServicePort.removeQuoteItem(itemId);
    }
    
    @MutationMapping
    public boolean deleteQuote(@Argument Long id) {
        quoteServicePort.deleteQuote(id);
        return true;
    }
}