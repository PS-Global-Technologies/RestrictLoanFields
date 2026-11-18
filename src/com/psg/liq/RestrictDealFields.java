package com.psg.liq;

import java.util.Iterator;
import java.util.List;

import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNCodeChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNCollectionChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNDateChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.ObjectEvent;
import com.misys.liq.bm.desktopcore.main.cdt.ObjectWithTIPs;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LiqMNLoanChangeTransaction;
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
		 LiqMNDealAdjustmentTransaction  dealChangeTransaction =  (LiqMNDealAdjustmentTransaction) (object.getTransaction());

		int current=dealChangeTransaction.getBorrowers().size();
		LoanIQ.logInfo("Current borrowers List size: ");
		int previous=dealChangeTransaction.getPreviousBorrowers().size();
		LoanIQ.logInfo("Previous borrowers List size: ");
		
				if (current!=previous) {
										ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
								Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
										+ "Borrowers Update in Loan IQ is restricted, Please use FCLS to do this event"),
								new Object[0])));

				
				
			}
			
			
		
	}
}
