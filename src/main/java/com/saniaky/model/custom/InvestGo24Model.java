package com.saniaky.model.custom;

import com.saniaky.model.BasicModel;
import org.apache.commons.lang3.StringUtils;

/**
 * http://investgo24.com/
 *
 * @author saniaky
 * @since 2/6/17
 */
public class InvestGo24Model extends BasicModel {

    private String description;
    private String fullDescription;
    private String contact;
    private String team;

    @Override
    public String getText() {
        return String.format(
                "Ссылка: %s\n\n" +
                        "Инициатор проекта, контакты:\n      %s\n\n" +
                        "Описание бизнес идеи:\n      %s\n\n" +
                        "Полное описание инвестиционного проекта:\n      %s\n\n",
                getUrl(),
                getContact(),
                getDescription(),
                getFullDescription());
    }


    public String getDescription() {
        return StringUtils.defaultIfEmpty(description, "-");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return StringUtils.defaultIfEmpty(contact, "-");
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTeam() {
        return StringUtils.defaultIfEmpty(team, "-");
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFullDescription() {
        return StringUtils.defaultIfEmpty(fullDescription, "-");
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
