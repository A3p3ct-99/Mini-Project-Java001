package org.example.constant;

import org.example.dto.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

import static org.example.constant.Color.*;

public class TableConfig {


    public static void displayProductTable(List<Product> products, int start, int end, int totalPages, int currentPage) {
        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);

        table.addCell(LIGHT_YELLOW + "ID", style);
        table.addCell(LIGHT_BLUE +  "Name" + RESET, style);
        table.addCell(LIGHT_CYAN +  "Unit Price", style);
        table.addCell(LIGHT_PURPLE + "Quantity", style);
        table.addCell(LIGHT_GREEN + "Imported Date", style);

        //set width column table
        table.setColumnWidth(0, 10, 10);
        table.setColumnWidth(1, 35, 35);
        table.setColumnWidth(2, 15, 15);
        table.setColumnWidth(3, 10, 10);
        table.setColumnWidth(4, 15, 15);

        if (!products.isEmpty()) {
            for (int i = start; i < end; i++) {
                table.addCell(LIGHT_GREEN + products.get(i).getId(), style);
                table.addCell(CYAN + ITALIC + products.get(i).getName() , style);
                table.addCell(RED + ITALIC + products.get(i).getPrice(), style);
                table.addCell(BLUE + products.get(i).getQuantity(), style);
                table.addCell(PINK + products.get(i).getDate(), style);
            }

            table.addCell("Page : " + YELLOW + currentPage + RESET + " of " + LIGHT_GREEN + (totalPages > 0 ? totalPages : 1) + RESET, style,2);
            table.addCell("Total Record : " + LIGHT_GREEN + products.size(), style, 3);
        } else {
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
        }
        System.out.println(table.render());
    }
    public static void displayUnsaveProductTable(List<Product> products){
        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);

        table.addCell(LIGHT_YELLOW + "ID", style);
        table.addCell(LIGHT_BLUE +  "Name" + RESET, style);
        table.addCell(LIGHT_CYAN +  "Unit Price", style);
        table.addCell(LIGHT_PURPLE + "Quantity", style);
        table.addCell(LIGHT_GREEN + "Imported Date", style);

        //set width column table
        table.setColumnWidth(0, 10, 10);
        table.setColumnWidth(1, 35, 35);
        table.setColumnWidth(2, 15, 15);
        table.setColumnWidth(3, 10, 10);
        table.setColumnWidth(4, 15, 15);

        if (!products.isEmpty()) {
            products.forEach(product -> {
                table.addCell(LIGHT_GREEN + product.getId(), style);
                table.addCell(CYAN + ITALIC + product.getName() , style);
                table.addCell(RED + ITALIC + product.getPrice(), style);
                table.addCell(BLUE + product.getQuantity(), style);
                table.addCell(PINK + product.getDate(), style);
            });
        } else {
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
            table.addCell("---", style);
        }
        System.out.println(table.render());
    }
}
