/**
 * Define a grammar called TemporalRelationshipGrammar
 */
 

grammar TemporalRelationshipGrammar;

// Grammar rules

expression : rtip_disjunction ;

//TEMP - will need to generalize to handle arbitrary combinations of conjunctions/disjunctions
//These rules assume that the expressions are already in disjunctive normal form
rtip_disjunction : rtip_conjunction |
				   rtip_conjunction (DISJUNCTION_TOKEN rtip_conjunction)+ ;

//rtip_conjunction :  '{' rtip '}' |
//      			  '{' rtip (CONJUNCTION_TOKEN rtip)+ '}' ;
rtip_conjunction :  '{' rtip (CONJUNCTION_TOKEN rtip)* '}' ;
//  LATER REPLACE WITH THIS:  rtip_conjunction :  rtip (CONJUNCTION_TOKEN rtip)*  ;


//END TEMP				   

rtip : rtip_type lower_delimiter lower_value ',' upper_value upper_delimiter ;

rtip_type : beginning_to_beginning | beginning_to_end | end_to_beginning | end_to_end ;

beginning_to_beginning :  BEGIN_TO_BEGIN_TOKEN ;
beginning_to_end : BEGIN_TO_END_TOKEN ;
end_to_beginning : END_TO_BEGIN_TOKEN ;
end_to_end : END_TO_END_TOKEN ;

lower_value : negative_infinity | 
			  numeric_value ;
upper_value : positive_infinity | 
	      	  numeric_value ;

numeric_value : duration_magnitude duration_unit_of_measure;

duration_magnitude : NUMBER_TOKEN;

lower_delimiter : lower_open_delimiter |
				  lower_closed_delimiter ;
upper_delimiter : upper_open_delimiter |
				  upper_closed_delimiter ;

		  
lower_open_delimiter : LOWER_OPEN_DELIMITER_TOKEN ;
lower_closed_delimiter : LOWER_CLOSED_DELIMITER_TOKEN ;
upper_open_delimiter : UPPER_OPEN_DELIMITER_TOKEN ;
upper_closed_delimiter : UPPER_CLOSED_DELIMITER_TOKEN ;


negative_infinity : NEG_INFINITY_TOKEN ;
positive_infinity : POS_INFINITY_TOKEN ;

duration_unit_of_measure : 	seconds |
							minutes |
							hours |
							days |
							weeks |
							months |
							years ;
				  			
seconds : SECONDS_TOKEN ;
minutes : MINUTES_TOKEN ;
hours : HOURS_TOKEN ;
days : DAYS_TOKEN ;
weeks : WEEKS_TOKEN ;
months : MONTHS_TOKEN ;
years : YEARS_TOKEN ;
 

// Token definitions

BEGIN_TO_BEGIN_TOKEN : 'BB' ;
BEGIN_TO_END_TOKEN : 'BE' ;
END_TO_BEGIN_TOKEN : 'EB' ;
END_TO_END_TOKEN : 'EE' ;

LOWER_OPEN_DELIMITER_TOKEN : '(';
LOWER_CLOSED_DELIMITER_TOKEN : '[' ;
UPPER_OPEN_DELIMITER_TOKEN : ')' ;
UPPER_CLOSED_DELIMITER_TOKEN : ']' ;

NEG_INFINITY_TOKEN : '-INF' ;
POS_INFINITY_TOKEN : '+INF' | 'INF' ;
NUMBER_TOKEN : ('+'|'-')? [0-9]+ ('.'[0-9]+)? ;

SECONDS_TOKEN : 	'sc' ;
MINUTES_TOKEN :  	'mn' ;
HOURS_TOKEN:		'hr' ;
DAYS_TOKEN:			'dy' ;
WEEKS_TOKEN:		'wk' ;
MONTHS_TOKEN:		'mo' ;
YEARS_TOKEN:		'yr' ;

CONJUNCTION_TOKEN : '&' ;
DISJUNCTION_TOKEN : '|' ;

WS : [ \r\t\n]+ -> skip ;