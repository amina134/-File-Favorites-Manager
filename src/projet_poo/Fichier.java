/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_poo;

import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fichier {  
    private Integer id;  
    private String chemin;  
    private String titre;  
    private String auteur;  
    private String resume;  
    private String commentaires;  
    private List<String> tags;  

    // Constructeurs  
    public Fichier() {  
        this.tags = new ArrayList<>();  
    }  

  public Fichier(int id, String chemin, String titre, String auteur, String resume, String commentaires) {
        this();
        this.id = id;
        this.chemin = chemin;
        this.titre = titre;
        this.auteur = auteur;
        this.resume = resume;
        this.commentaires = commentaires;
    } 

    // Getters et setters  
    public Integer getId() {  
        return id;  
    }  

    public void setId(Integer id) {  
        this.id = id;  
    }  

    public String getChemin() {  
        return chemin;  
    }  

    public void setChemin(String chemin) {  
        this.chemin = chemin;  
    }  

    public String getTitre() {  
        return titre;  
    }  

    public void setTitre(String titre) {  
        this.titre = titre;  
    }  

    public String getAuteur() {  
        return auteur;  
    }  

    public void setAuteur(String auteur) {  
        this.auteur = auteur;  
    }  

    public String getResume() {  
        return resume;  
    }  

    public void setResume(String resume) {  
        this.resume = resume;  
    }  

    public String getCommentaires() {  
        return commentaires;  
    }  

    public void setCommentaires(String commentaires) {  
        this.commentaires = commentaires;  
    }  

    public List<String> getTags() {  
        return tags;  
    }  

    public void setTags(List<String> tags) {  
        this.tags = tags;  
    }  

    public void addTag(String tag) {  
        if (tag != null && !tag.trim().isEmpty()) {  
            this.tags.add(tag.trim());  
        }  
    }  

    public boolean isValid() {  
        return chemin != null && !chemin.isEmpty()   
                && titre != null && !titre.isEmpty()  
                && !tags.isEmpty();  
    } 
    
    
    
}