package com.saniaky.model.custom;

import com.saniaky.model.BasicModel;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * https://startup.ua/
 *
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class StartupUAModel extends BasicModel {

    private String idea;
    private String presetStatus;
    private String market;
    private String problemsAndPossibilities;
    private String product;                 // Решение
    private String competitors;
    private String benefits;
    private String finance;
    private String businessModel;
    private String proposalGoal;            // Целевое назначение инвестиций
    private String proposal;                // Предложение инвестору

    public StartupUAModel(String url, String title, String text, String imageUrl, Collection<String> keywords) {
        super(url, title, text, imageUrl, keywords);
    }

    @Override
    public String getText() {
        return String.format("Идея:%n      %s%n%n" +
                        "Текущее состояние:%n      %s%n%n" +
                        "Рынок:%n      %s%n%n" +
                        "Проблема или Возможность:%n      %s%n%n" +
                        "Решение:%n      %s%n%n" +
                        "Конкуренты:%n      %s%n%n" +
                        "Преимущества или дифференциаторы:%n      %s%n%n" +
                        "Финансы:%n      %s%n%n" +
                        "Бизнес-модель:%n      %s%n%n" +
                        "Целевое назначение инвестиций:%n      %s%n%n" +
                        "Предложение инвестору:%n      %s%n%n",
                getIdea(),
                getPresetStatus(),
                getMarket(),
                getProblemsAndPossibilities(),
                getProduct(),
                getCompetitors(),
                getBenefits(),
                getFinance(),
                getBusinessModel(),
                getProposalGoal(),
                getProposal());
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getPresetStatus() {
        return StringUtils.defaultIfEmpty(presetStatus, "");
    }

    public void setPresetStatus(String presetStatus) {
        this.presetStatus = presetStatus;
    }

    public String getMarket() {
        return StringUtils.defaultIfEmpty(market, "");
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getProblemsAndPossibilities() {
        return StringUtils.defaultIfEmpty(problemsAndPossibilities, "");
    }

    public void setProblemsAndPossibilities(String problemsAndPossibilities) {
        this.problemsAndPossibilities = problemsAndPossibilities;
    }

    public String getProduct() {
        return StringUtils.defaultIfEmpty(product, "");
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCompetitors() {
        return StringUtils.defaultIfEmpty(competitors, "");
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

    public String getBenefits() {
        return StringUtils.defaultIfEmpty(benefits, "");
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getFinance() {
        return StringUtils.defaultIfEmpty(finance, "");
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getBusinessModel() {
        return StringUtils.defaultIfEmpty(businessModel, "");
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getProposalGoal() {
        return StringUtils.defaultIfEmpty(proposalGoal, "");
    }

    public void setProposalGoal(String proposalGoal) {
        this.proposalGoal = proposalGoal;
    }

    public String getProposal() {
        return StringUtils.defaultIfEmpty(proposal, "");
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }
}
