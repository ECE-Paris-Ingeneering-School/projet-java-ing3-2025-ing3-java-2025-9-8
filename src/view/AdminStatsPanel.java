package view;

import dao.SalesDAO;
import javax.swing.*;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class AdminStatsPanel extends JPanel {

    public AdminStatsPanel() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet 1 : Évolution du chiffre d'affaires (courbe)
        ChartPanel caChartPanel = createCACurveChartPanel();
        tabbedPane.addTab("Chiffre d'affaires", caChartPanel);

        // Onglet 2 : Répartition des ventes par produit (camembert)
        ChartPanel productPieChartPanel = createSalesByProductChartPanel();
        tabbedPane.addTab("Répartition ventes", productPieChartPanel);

        // Onglet 3 : Taux de remplissage des sessions (barres)
        ChartPanel occupancyChartPanel = createOccupancyChartPanel();
        tabbedPane.addTab("Taux de remplissage", occupancyChartPanel);

        // Onglet 4 : Comparaison Sessions vs Boutique (barres groupées)
        ChartPanel comparisonChartPanel = createSessionVsBoutiqueChartPanel();
        tabbedPane.addTab("Sessions vs Boutique", comparisonChartPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private ChartPanel createCACurveChartPanel() {
        DefaultCategoryDataset dataset = SalesDAO.getMonthlyRevenueDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Évolution du Chiffre d'Affaires",
                "Mois",
                "CA (€)",
                dataset);
        return new ChartPanel(chart);
    }

    private ChartPanel createSalesByProductChartPanel() {
        DefaultPieDataset dataset = SalesDAO.getSalesByProductDataset();
        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des Ventes",
                dataset,
                true,   // affiche la légende
                true,
                false);
        return new ChartPanel(chart);
    }

    private ChartPanel createOccupancyChartPanel() {
        DefaultCategoryDataset dataset = SalesDAO.getOccupancyDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Taux de Remplissage des Sessions",
                "Session",
                "Taux (%)",
                dataset);
        return new ChartPanel(chart);
    }

    private ChartPanel createSessionVsBoutiqueChartPanel() {
        DefaultCategoryDataset dataset = SalesDAO.getSessionVsBoutiqueDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Ventes : Sessions vs Boutique",
                "Catégorie",
                "Ventes (€)",
                dataset);
        return new ChartPanel(chart);
    }
}
