# Sample drools application

The application validates a list of products against a list of rules.
Each product has an id, name, price and a list of categories.
Each category has a name.
The validation service also consumes product stock information.

**Validation rules**
- Error: Each product must have a non-null name.
- Error: Each product must have a positive price.
- Error: Each product must be associated to at least one category.
- Warning: A product can't be assigned to both 'Category 1' and 'Category 2'.
- Warning: A product must have a positive stock.

**Relevant documentation**
- [KIE Sessions](https://docs.jboss.org/drools/release/7.38.0.Final/drools-docs/html_single/index.html#kie-sessions-con_decision-engine)
- [Rule conditions](https://docs.jboss.org/drools/release/7.38.0.Final/drools-docs/html_single/index.html#drl-rules-WHEN-con_drl-rules)