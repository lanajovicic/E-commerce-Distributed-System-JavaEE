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
public class StavkaKorpePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKorpa")
    private int idKorpa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdArt")
    private int idArt;

    public StavkaKorpePK() {
    }

    public StavkaKorpePK(int idKorpa, int idArt) {
        this.idKorpa = idKorpa;
        this.idArt = idArt;
    }

    public int getIdKorpa() {
        return idKorpa;
    }

    public void setIdKorpa(int idKorpa) {
        this.idKorpa = idKorpa;
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
        hash += (int) idKorpa;
        hash += (int) idArt;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaKorpePK)) {
            return false;
        }
        StavkaKorpePK other = (StavkaKorpePK) object;
        if (this.idKorpa != other.idKorpa) {
            return false;
        }
        if (this.idArt != other.idArt) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaKorpePK[ idKorpa=" + idKorpa + ", idArt=" + idArt + " ]";
    }
    
}
