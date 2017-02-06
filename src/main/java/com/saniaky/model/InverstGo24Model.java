package com.saniaky.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author saniaky
 * @since 2/6/17
 */
public class InverstGo24Model extends BasicModel {

    String description;
    String fullDescription;
    String contact;
    String team;

    @Override
    public String getText() {
        return String.format("Инициатор проекта, контакты:\n      %s\n\n" +
                        "Описание бизнес идеи:\n      %s\n\n" +
                        "Полное описание инвестиционного проекта:\n      %s\n\n",
                contact,
                description,
                fullDescription);
    }


    public String getDescription() {
        return StringUtils.defaultIfEmpty(fullDescription, "");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return StringUtils.defaultIfEmpty(fullDescription, "");
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTeam() {
        return StringUtils.defaultIfEmpty(fullDescription, "");
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFullDescription() {
        return StringUtils.defaultIfEmpty(fullDescription, "");
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
