/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Embeddable
public class StavkaListeZeljaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdListe")
    private int idListe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdArt")
    private int idArt;

    public StavkaListeZeljaPK() {
    }

    public StavkaListeZeljaPK(int idListe, int idArt) {
        this.idListe = idListe;
        this.idArt = idArt;
    }

    public int getIdListe() {
        return idListe;
    }

    public void setIdListe(int idListe) {
        this.idListe = idListe;
    }

    public int getIdArt() {
        return idArt;
    }

    public void setIdArt(int idArt) {
        this.idArt = idArt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idListe;
        hash += (int) idArt;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaListeZeljaPK)) {
            return false;
        }
        StavkaListeZeljaPK other = (StavkaListeZeljaPK) object;
        if (this.idListe != other.idListe) {
            return false;
        }
        if (this.idArt != other.idArt) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaListeZeljaPK[ idListe=" + idListe + ", idArt=" + idArt + " ]";
    }
    
}
