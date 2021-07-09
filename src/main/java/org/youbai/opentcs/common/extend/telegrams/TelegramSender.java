/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.common.extend.telegrams;

/**
 * Declares methods for comm adapters capable of sending telegrams/requests.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface TelegramSender {

  /**
   * Sends the given {@link Request}.
   *
   * @param request The {@link Request} to be sent.
   */
  void sendTelegram(Request request);
}
