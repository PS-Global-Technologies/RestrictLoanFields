package com.psg.liq;

import java.util.Objects;

import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.bm.desktopcore.main.cdt.ObjectEvent;
import com.misys.liq.bm.desktopcore.main.deal.LiqMNDealAdjustmentTransaction;
import com.misys.liq.bm.main.goodies.LiqMNModelEnhancementRegistryEvent;
import com.misys.liq.bm.main.liqmaineventmanagementsubapp.LiqMNBusinessEventTrigger;
import com.misys.liq.bm.product.ProductEvent;
import com.misys.liq.inf.LiqEventAction;
import com.misys.liq.inf.LiqEventActionContext;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;

public class RestrictDealFields implements LiqEventAction {

	static String strPropertyFilePath = "ls2cust.ini";

	@Override
	public void execute(LiqMNModelEnhancementRegistryEvent evt, LiqMNBusinessEventTrigger trigger,
			LiqEventActionContext context) {

		ObjectEvent event = (ObjectEvent) (evt.eventOwner());
		ProductEvent object = (ProductEvent) event;
		LiqMNDealAdjustmentTransaction dealChangeTransaction = (LiqMNDealAdjustmentTransaction) (object
				.getTransaction());

		int current = dealChangeTransaction.getBorrowers().size();
		LoanIQ.logInfo("Current borrowers List size: ");
		int previous = dealChangeTransaction.getPreviousBorrowers().size();
		LoanIQ.logInfo("Previous borrowers List size: ");

		if (current == previous) {
			ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
					Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
							+ "Borrowers Update in Loan IQ is restricted, Please use FCLS to do this event"),
					new Object[0])));

		} else {
			if (dealChangeTransaction.getPreviousGracePeriod() == dealChangeTransaction.getDeal().gracePeriod) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Grace Period Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}

			else if (!isDateChanged(dealChangeTransaction.getPreviousSblcIssuanceDate(),
					(LiqDate) dealChangeTransaction.getDeal().getSblcIssuanceDate())) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "SBLC Issuance Date Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));

			}

			else if (!isDateChanged(dealChangeTransaction.getPreviousSblcIssuanceDate(),
					(LiqDate) dealChangeTransaction.getDeal().getSblcUsageExpirationDate())) {

				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "SBLC Usage Expiration Date Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));

			}
			/*
			 * else if (!isDateChanged(dealChangeTransaction.facility (LiqDate)
			 * dealChangeTransaction.getDeal().zz_facilityAddCutoffDate())) {
			 * 
			 * ExceptionUtility.throwException((Throwable) new
			 * LiqError(StringUtility.bindWith(
			 * Messages.liqNlsExternalizedMessage("GIB Internal Validation: " +
			 * "SBLC Usage Expiration Date Update in Loan IQ is restricted, Please use FCLS to do this event"
			 * ), new Object[0])));
			 * 
			 * }
			 */

			else if (!(dealChangeTransaction.getPreviousExpenseCode()
					.equals(dealChangeTransaction.getDeal().getExpenseCode()))) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Expense Code Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}

			
			else if ((dealChangeTransaction.getPreviousMaximumNumberOfLoans()
				==(dealChangeTransaction.getDeal().getMaximumNumberOfLoans()))) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Max Drawdawn Amount Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));

			} else if ((dealChangeTransaction.getPreviousMaximumNumberOfMatchFundedLoans()
					==(dealChangeTransaction.getDeal().getMaximumNumberOfMatchFundedLoans()))) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Min Drawdawn Amount Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			else if (!Objects.equals(
			dealChangeTransaction.autoCharge(),dealChangeTransaction.getDeal().autoCharge())) {
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Auto Charge Pre Payment Fee Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			else if (Objects.equals(
			        dealChangeTransaction.getPreviousReserveAdjustedRatesIndicator(),
			        dealChangeTransaction.getReserveAdjustedRatesIndicator())) {
				
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Reserve Adjusted Rates Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			else if (Objects.equals(
			        dealChangeTransaction.getPreviousActualIndicator(),
			        dealChangeTransaction.getDeal().getActualIndicator())) {
				
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Previous Circle Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			
			else if (!Objects.equals(
			        dealChangeTransaction.isAutomaticPaymentApplicationLogicPayment(),
			        dealChangeTransaction.getDeal().isAutomaticPaymentApplicationLogicPayment())) {
				
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Auto Apply Penalty Pay Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			else if (!Objects.equals(
			        dealChangeTransaction.zz_euroInterimRoundingPrecision(),
			        dealChangeTransaction.getDeal().zz_euroInterimRoundingPrecision())) {
				
				ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
						Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
								+ "Euro Interim Amount Precision in Loan IQ is restricted, Please use FCLS to do this event"),
						new Object[0])));
			}
			
			

			/*
			 * else if (Objects.equals( dealChangeTransaction.mail,
			 * dealChangeTransaction.getDeal().getDoNotMailBillIndicator())) {
			 * 
			 * ExceptionUtility.throwException((Throwable) new
			 * LiqError(StringUtility.bindWith(
			 * Messages.liqNlsExternalizedMessage("GIB Internal Validation: " +
			 * "DoNot mail Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"
			 * ), new Object[0]))); }
			 * 
			 * else if (Objects.equals( dealChangeTransaction.getDoNotPrintBillIndicator(),
			 * dealChangeTransaction.getDeal().getDoNotPrintBillIndicator())) {
			 * 
			 * ExceptionUtility.throwException((Throwable) new
			 * LiqError(StringUtility.bindWith(
			 * Messages.liqNlsExternalizedMessage("GIB Internal Validation: " +
			 * "DoNot Print Indicator Update in Loan IQ is restricted, Please use FCLS to do this event"
			 * ), new Object[0]))); }
			 */			
			
		}
	}

	public boolean isDateChanged(LiqDate oldDate, LiqDate newDate) {
		return !Objects.equals(oldDate, newDate);
	}
}
