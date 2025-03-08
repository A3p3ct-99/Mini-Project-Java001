package org.example.constant;

import org.example.dto.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.File;
import java.util.List;

import static org.example.constant.Color.*;

public class TableConfig {


    public static void displayProductTable(List<Product> products, int start, int end, int totalPages, int currentPage) {
        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        printHeaderTable(table, style);
        if (!products.isEmpty()) {
            for (int i = start; i < end; i++) {
                table.addCell(LIGHT_GREEN + products.get(i).getId(), style);
                table.addCell(CYAN + ITALIC + products.get(i).getName() , style);
                table.addCell(RED + ITALIC + "$" + products.get(i).getPrice(), style);
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

    public static void displayProductByIdAndName(Product product) {
        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        printHeaderTable(table, style);
        table.addCell(LIGHT_GREEN + product.getId(), style);
        table.addCell(CYAN + ITALIC + product.getName() , style);
        table.addCell(RED + ITALIC + "$" + product.getPrice(), style);
        table.addCell(BLUE + product.getQuantity(), style);
        table.addCell(PINK + product.getDate(), style);
        System.out.println(table.render());
    }

    public static void displayBackUpDataTable(File[] files) {
        Table table = new Table(2, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        table.addCell(LIGHT_CYAN + "List of Backup Data", style, 2);
        table.setColumnWidth(0, 6, 6);
        table.setColumnWidth(1, 40, 40);
        int i = 0;
        if (files.length == 0) {
            table.addCell("-", style);
            table.addCell("----", style);
        } else {
            for (File file : files) {
                table.addCell(LIGHT_GREEN + (++i), style);
                table.addCell(LIGHT_PURPLE + file.getName(), style);
            }
        }
        System.out.println(table.render());
    }

    private static void printHeaderTable(Table table, CellStyle style) {
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
    }

    public static void printFooterTable() {
        System.out.println("\t".repeat(8) + "----------- Menu -----------");
        System.out.printf("\t" + "   %sN.%s Next Page      %sP.%s Previous Page      %sF.%s First Page      %sL.%s Last Page      %sG.%s Goto\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.println();
        System.out.printf("%sW)%s Write        %sR)%s Read (id)     %sU)%s Update        %sD)%s Delete        %sS)%s Search (name)        %sSe)%s Set rows\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.printf("%ssa)%s Save        %sUn)%s Unsaved      %sBa)%s Backup       %sRe)%s Restore      %sE)%s Exit\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.println("\t".repeat(8) + "----------------------------");
    }
}
