/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange;

import org.youbai.opentcs.drivers.adapter.telegrams.OrderRequest;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renders order telegrams when displayed in a list.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class OrderListCellRenderer
    extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList<?> list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
    Component component = super.getListCellRendererComponent(list,
                                                             value,
                                                             index,
                                                             isSelected,
                                                             cellHasFocus);

    if (value instanceof OrderRequest) {
      OrderRequest request = (OrderRequest) value;
      JLabel label = (JLabel) component;

      StringBuilder sb = new StringBuilder();
      sb.append('#');
      sb.append(request.getOrderId());
      sb.append(": Dest.: ");
      sb.append(request.getDestinationId());
      sb.append(", Act.: ");
      sb.append(request.getDestinationAction());
      sb.append("...");

      label.setText(sb.toString());
    }

    return component;
  }
}
