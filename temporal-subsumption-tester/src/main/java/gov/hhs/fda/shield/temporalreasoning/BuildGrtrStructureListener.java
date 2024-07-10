package gov.hhs.fda.shield.temporalreasoning;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import gov.hhs.fda.shield.temporalreasoning.utils.Converters;

public class BuildGrtrStructureListener extends TemporalRelationshipGrammarBaseListener  {
	
	boolean DEBUG = false;
	
	private Stack<RtipDisjunction> currentRtipDisjunction = new Stack<RtipDisjunction>();
	private Stack<RtipConjunction> currentRtipConjunction = new Stack<RtipConjunction>();
	private Stack<Rtip> currentRtip = new Stack<Rtip>();
	private Stack<Float> temporalMagnitudes = new Stack<Float>();
	private Stack<TemporalUom> temporalUnitsOfMeasure = new Stack<TemporalUom>();
	
	public BuildGrtrStructureListener() {
		// TODO Auto-generated constructor stub
	}

	@Override public void enterExpression(TemporalRelationshipGrammarParser.ExpressionContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
		// Currently, every expression is a RtipDisjunction, so that rule creates the expression structure
	}

	@Override public void exitExpression(TemporalRelationshipGrammarParser.ExpressionContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
		// Currently, every expression is a RtipDisjunction, so that rule creates the expression structure
	}
	
	@Override public void enterRtip_disjunction(TemporalRelationshipGrammarParser.Rtip_disjunctionContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
		currentRtipDisjunction.push(new RtipDisjunction());
	}

	@Override public void exitRtip_disjunction(TemporalRelationshipGrammarParser.Rtip_disjunctionContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
//Printers.print(currentRtipDisjunction.peek(), 0);
		TemporalExpressionParser.builtTemporalStructure = currentRtipDisjunction.pop();  // set return value to most recent disjunction
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRtip_conjunction(TemporalRelationshipGrammarParser.Rtip_conjunctionContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
		currentRtipConjunction.push(new RtipConjunction());
		currentRtipDisjunction.peek().addRtipConjunction(currentRtipConjunction.peek()); // add conjunction to list within RtipDisjunction
	}

	@Override public void exitRtip_conjunction(TemporalRelationshipGrammarParser.Rtip_conjunctionContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
		currentRtipConjunction.pop();
	}

	@Override public void enterRtip(TemporalRelationshipGrammarParser.RtipContext ctx) { 
if (DEBUG) if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
		currentRtip.push(new Rtip(ctx.getText()));
		currentRtipConjunction.peek().addRtip(currentRtip.peek());  // add rtip to list within RtipConjunction
	}

	@Override public void exitRtip(TemporalRelationshipGrammarParser.RtipContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
//		Printers.print(currentRtip.peek(), 0);
		currentRtip.pop();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRtip_type(TemporalRelationshipGrammarParser.Rtip_typeContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
		// The RtipType value is set in the specific rule that recognizes the denoted type (see below)
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRtip_type(TemporalRelationshipGrammarParser.Rtip_typeContext ctx) { 
		// The RtipType value is set in the specific rule that recognizes the denoted type (see below)
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBeginning_to_beginning(TemporalRelationshipGrammarParser.Beginning_to_beginningContext ctx) { 
//if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());

	}

	@Override public void exitBeginning_to_beginning(TemporalRelationshipGrammarParser.Beginning_to_beginningContext ctx) { 
		currentRtip.peek().setRtipType(RtipType.BEG_BEG);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBeginning_to_end(TemporalRelationshipGrammarParser.Beginning_to_endContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());

	}

	@Override public void exitBeginning_to_end(TemporalRelationshipGrammarParser.Beginning_to_endContext ctx) { 
		currentRtip.peek().setRtipType(RtipType.BEG_END);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEnd_to_beginning(TemporalRelationshipGrammarParser.End_to_beginningContext ctx) { }

	@Override public void exitEnd_to_beginning(TemporalRelationshipGrammarParser.End_to_beginningContext ctx) { 
		currentRtip.peek().setRtipType(RtipType.END_BEG);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEnd_to_end(TemporalRelationshipGrammarParser.End_to_endContext ctx) { }

	@Override public void exitEnd_to_end(TemporalRelationshipGrammarParser.End_to_endContext ctx) { 
		currentRtip.peek().setRtipType(RtipType.END_END);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLower_value(TemporalRelationshipGrammarParser.Lower_valueContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());

	}

	@Override public void exitLower_value(TemporalRelationshipGrammarParser.Lower_valueContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
		if (!currentRtip.peek().isLowerValueInfinite()) {
//System.out.println("Converting to seconds: " + temporalMagnitudes.peek() + " and " + temporalUnitsOfMeasure.peek());
			if (!temporalMagnitudes.empty() && !temporalUnitsOfMeasure.empty() ) {
				currentRtip.peek().setLowerValueSeconds(Converters.convertToSeconds(
														  temporalMagnitudes.pop(),
														  temporalUnitsOfMeasure.pop()));
			}
			else  { 
				/*throw error here*/ 			
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpper_value(TemporalRelationshipGrammarParser.Upper_valueContext ctx) { 
if (DEBUG) System.out.println("Entering: " + ctx.getClass().getSimpleName());
	}

	@Override public void exitUpper_value(TemporalRelationshipGrammarParser.Upper_valueContext ctx) { 
if (DEBUG) System.out.println("Exiting: " + ctx.getClass().getSimpleName());
		if (!currentRtip.peek().isUpperValueInfinite()) {
			if (!temporalMagnitudes.empty() && !temporalUnitsOfMeasure.empty() ) {
				currentRtip.peek().setUpperValueSeconds(Converters.convertToSeconds(
														  temporalMagnitudes.pop(),
														  temporalUnitsOfMeasure.pop()));
			}
			else  { 
System.out.println("Error:  temporalMagnitues or temporalUnitsOfMeasure is empty");	
			}
		}

	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNumeric_value(TemporalRelationshipGrammarParser.Numeric_valueContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNumeric_value(TemporalRelationshipGrammarParser.Numeric_valueContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDuration_magnitude(TemporalRelationshipGrammarParser.Duration_magnitudeContext ctx) { }

	@Override public void exitDuration_magnitude(TemporalRelationshipGrammarParser.Duration_magnitudeContext ctx) { 
		Float durationMagnitude = Float.parseFloat(ctx.getText());
		if (durationMagnitude.equals(Float.parseFloat("-0"))) {
			durationMagnitude = Float.parseFloat("0");  // special case, because 0 != -0 for Java Float
		}
		temporalMagnitudes.push(durationMagnitude);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLower_delimiter(TemporalRelationshipGrammarParser.Lower_delimiterContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLower_delimiter(TemporalRelationshipGrammarParser.Lower_delimiterContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpper_delimiter(TemporalRelationshipGrammarParser.Upper_delimiterContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitUpper_delimiter(TemporalRelationshipGrammarParser.Upper_delimiterContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLower_open_delimiter(TemporalRelationshipGrammarParser.Lower_open_delimiterContext ctx) { }

	@Override public void exitLower_open_delimiter(TemporalRelationshipGrammarParser.Lower_open_delimiterContext ctx) { 
		currentRtip.peek().setLowerIntervalOpen(true);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLower_closed_delimiter(TemporalRelationshipGrammarParser.Lower_closed_delimiterContext ctx) { }

	@Override public void exitLower_closed_delimiter(TemporalRelationshipGrammarParser.Lower_closed_delimiterContext ctx) { 
		currentRtip.peek().setLowerIntervalOpen(false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpper_open_delimiter(TemporalRelationshipGrammarParser.Upper_open_delimiterContext ctx) { }

	@Override public void exitUpper_open_delimiter(TemporalRelationshipGrammarParser.Upper_open_delimiterContext ctx) { 
		currentRtip.peek().setUpperIntervalOpen(true);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpper_closed_delimiter(TemporalRelationshipGrammarParser.Upper_closed_delimiterContext ctx) { }

	@Override public void exitUpper_closed_delimiter(TemporalRelationshipGrammarParser.Upper_closed_delimiterContext ctx) { 
		currentRtip.peek().setUpperIntervalOpen(false);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNegative_infinity(TemporalRelationshipGrammarParser.Negative_infinityContext ctx) { }

	@Override public void exitNegative_infinity(TemporalRelationshipGrammarParser.Negative_infinityContext ctx) { 
		currentRtip.peek().setLowerValueInfinite(true);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPositive_infinity(TemporalRelationshipGrammarParser.Positive_infinityContext ctx) { }

	@Override public void exitPositive_infinity(TemporalRelationshipGrammarParser.Positive_infinityContext ctx) { 
		currentRtip.peek().setUpperValueInfinite(true);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDuration_unit_of_measure(TemporalRelationshipGrammarParser.Duration_unit_of_measureContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDuration_unit_of_measure(TemporalRelationshipGrammarParser.Duration_unit_of_measureContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSeconds(TemporalRelationshipGrammarParser.SecondsContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSeconds(TemporalRelationshipGrammarParser.SecondsContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.SEC);

	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMinutes(TemporalRelationshipGrammarParser.MinutesContext ctx) { }

	@Override public void exitMinutes(TemporalRelationshipGrammarParser.MinutesContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.MIN);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterHours(TemporalRelationshipGrammarParser.HoursContext ctx) { }

	@Override public void exitHours(TemporalRelationshipGrammarParser.HoursContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.HOUR);

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDays(TemporalRelationshipGrammarParser.DaysContext ctx) { }

	@Override public void exitDays(TemporalRelationshipGrammarParser.DaysContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.DAY);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWeeks(TemporalRelationshipGrammarParser.WeeksContext ctx) { }

	@Override public void exitWeeks(TemporalRelationshipGrammarParser.WeeksContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.WEEK);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMonths(TemporalRelationshipGrammarParser.MonthsContext ctx) { }

	@Override public void exitMonths(TemporalRelationshipGrammarParser.MonthsContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.MONTH);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterYears(TemporalRelationshipGrammarParser.YearsContext ctx) { }

	@Override public void exitYears(TemporalRelationshipGrammarParser.YearsContext ctx) { 
		temporalUnitsOfMeasure.push(TemporalUom.YEAR);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { 
	}
}
