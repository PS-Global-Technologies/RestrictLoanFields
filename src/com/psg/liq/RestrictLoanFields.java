package com.psg.liq;

import java.util.Iterator;
import java.util.List;

import com.misys.liq.Messages;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNCodeChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNCollectionChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.LiqMNDateChangeItem;
import com.misys.liq.bm.desktopcore.main.cdt.ObjectEvent;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LiqMNLoanChangeTransaction;
import com.misys.liq.bm.main.goodies.LiqMNModelEnhancementRegistryEvent;
import com.misys.liq.bm.main.liqmaineventmanagementsubapp.LiqMNBusinessEventTrigger;
import com.misys.liq.inf.LiqEventAction;
import com.misys.liq.inf.LiqEventActionContext;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;

public class RestrictLoanFields implements LiqEventAction {

	static String strPropertyFilePath = "ls2cust.ini";

	@Override
	public void execute(LiqMNModelEnhancementRegistryEvent evt, LiqMNBusinessEventTrigger trigger,
			LiqEventActionContext context) {

		ObjectEvent event = (ObjectEvent) (evt.eventOwner());
		LiqMNLoanChangeTransaction loanChangeTransaction = (LiqMNLoanChangeTransaction) (event.evoOwner());
		List changeItemsList = loanChangeTransaction.getChangeItems();

		LiqDate loanChangeNewMaturityDate = null;

		String strRepricingFrequency = null;
		LiqDate oldMaturityDate = null;
		for (int i = 0; i < changeItemsList.size(); i++) {
			Object obj = changeItemsList.get(i);
			if (obj instanceof LiqMNDateChangeItem) {
				LiqMNDateChangeItem item = (LiqMNDateChangeItem) obj;
				String strFldCode = item.fieldCode;

				if ("MATDT".equals(strFldCode)) {
					loanChangeNewMaturityDate = item.newDateValue;
					oldMaturityDate = item.previousDateValue;
					System.out.print("\n\nMaturity Date New: " + loanChangeNewMaturityDate.toString());
					System.out.print("\nMaturity Date Old: " + oldMaturityDate.toString());
					if (!item.newDateValue.equals(item.previousDateValue)) {

						ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
								Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
										+ "Maturity Change is restricted, Please use FCLS to do this event"),
								new Object[0])));

					}

				}

			}
			
			else if (obj instanceof LiqMNCodeChangeItem) {
				LiqMNCodeChangeItem item = (LiqMNCodeChangeItem) obj;
				String strFldCode = item.fieldCode;

				if ("RSKTP".equals(strFldCode)) {
					System.out.print("\n\nRisk Type  New: " + item.newTextValue.toString());
					System.out.print("\nRisk Type Old: " + item.previousTextValue.toString());
					if (!item.newTextValue.toString().equals(item.previousTextValue.toString())) {

						ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
								Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
										+ "Risk Type Change is restricted, Please use FCLS to do this event"),
								new Object[0])));

					}

				}

			}

			else if (obj instanceof LiqMNCollectionChangeItem) {
				LiqMNCollectionChangeItem item = (LiqMNCollectionChangeItem) obj;
				String strFldCode = item.fieldCode;

				if ("ACVAL".equals(strFldCode)) {

					Iterator iterator = ((List) item.children()).iterator();
					while (iterator.hasNext()) {
						Object object = iterator.next();
						if (object instanceof LiqMNDateChangeItem) {

							LiqMNDateChangeItem date = (LiqMNDateChangeItem) object;
							strFldCode = ((LiqMNDateChangeItem) date).fieldCode;

							if ("ACADD".equals(strFldCode)) {

								LiqDate loanChangeNewAdjustedDueDate = date.newDateValue;
								LiqDate loanChangeOldAdjustedDueDate = date.previousDateValue;

								System.out.print(
										"\n\nAdjusted Due Date New: " + loanChangeNewAdjustedDueDate.toString());
								System.out.print(
										"\nAdjusted Due Date Old: " + loanChangeOldAdjustedDueDate.toString());

								if (!date.newDateValue.equals(date.previousDateValue)) {

									ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
											Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
													+ "Accrual Adjusted Due Date is restricted, Please use FCLS to do this event"),
											new Object[0])));

								}

							}

							if ("ACAED".equals(strFldCode)) {

								LiqDate loanChangeNewEndDate = date.newDateValue;
								LiqDate loanChangeOldEndDate = date.previousDateValue;

								System.out.print("\n\nEnd Date New: " + loanChangeNewEndDate.toString());
								System.out.print("\nEnd Date Old: " + loanChangeOldEndDate.toString());

								if (!date.newDateValue.equals(date.previousDateValue)) {

									ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
											Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
													+ "Accrual End Date is restricted, Please use FCLS to do this event"),
											new Object[0])));

								}

							}

							if ("ACACD".equals(strFldCode)) {

								LiqDate loanChangeNewDueDate = date.newDateValue;
								LiqDate loanChangeOldDueDate = date.previousDateValue;

								System.out.print("\n\nDue Date New: " + loanChangeNewDueDate.toString());
								System.out.print("\nDue Date Old: " + loanChangeOldDueDate.toString());

								if (!date.newDateValue.equals(date.previousDateValue)) {

									ExceptionUtility.throwException((Throwable) new LiqError(StringUtility.bindWith(
											Messages.liqNlsExternalizedMessage("GIB Internal Validation: "
													+ "Accrual Due Date Change is restricted, Please use FCLS to do this event"),
											new Object[0])));

								}

							}

						}
					}
				}
			} else {

				// do nothing
			}

		}
	}
}
