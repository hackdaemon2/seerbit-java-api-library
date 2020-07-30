/*
 * Copyright (C) 2020 Seerbit
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.seerbit.v2.service;

import com.google.gson.JsonObject;
import com.seerbit.v2.model.CardPayment;
import com.seerbit.v2.model.CardPreAuth;
import com.seerbit.v2.model.PaymentCancel;
import com.seerbit.v2.model.PaymentCapture;
import com.seerbit.v2.model.PaymentRefund;
import com.seerbit.v2.model.Transaction;

/**
 * @author Seerbit
 */
public interface CardService {

	/**
	 * @param card A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doAuthorize(CardPayment card);

	/**
	 * @param transaction A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doValidate(Transaction transaction);

	/**
	 * @param cardPreAuth A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPreauthAuthorization(CardPreAuth cardPreAuth);

	/**
	 * @param paymentCapture A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentCapture(PaymentCapture paymentCapture);

	/**
	 * @param paymentRefund A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentRefund(PaymentRefund paymentRefund);

	/**
	 * @param paymentCancel A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentCancel(PaymentCancel paymentCancel);

	/**
	 * @param paymentCharge A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentChargeNon3D(CardPayment paymentCharge);

	/**
	 * @param paymentCharge A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doTokenize(CardPayment paymentCharge);

	/**
	 * @param paymentCharge A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentCharge3DS(CardPayment paymentCharge);

	/**
	 * @param paymentCharge A non-optional class, the payload
	 *
	 * @return JsonObject
	 */
	JsonObject doPaymentCharge3D(CardPayment paymentCharge);
}
