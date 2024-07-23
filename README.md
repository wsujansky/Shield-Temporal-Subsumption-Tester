This package implements the formal model for representing temporal relationships and for testing subsumption between two temporal relationships as specified in the following technical paper:  https://medrxiv.org/cgi/content/short/2023.11.17.23298715v1

The principal method in this package is 

                    public boolean subsumes(String predicateExpression, String candidateExpression)

which takes as input two formatted expressions, each representing a temporal relationship between two events, and determines whether the first expression subsumes the second expression.

Examples of such formatted expression to represent temporal relationships are:

      { BE(0sc,+1mo] }                                                    
      { EB(+4wk,+2mo) & BB(0dy,+INF) & EE(-INF, 0yr)  }                   
      { BE(0sc,+1mo] } |  { EB(+4wk,+2mo) }                               
      { BB[0wk,+13wk] & EE[0sc,0sc] } | { BB[27wk, +INF) & EE[0sc,0sc] }

See the technical paper cited above for the syntax and semantics of these expressions.
