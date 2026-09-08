/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "stavka_korpe")
@NamedQueries({
    @NamedQuery(name = "StavkaKorpe.findAll", query = "SELECT s FROM StavkaKorpe s")})
public class StavkaKorpe implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StavkaKorpePK stavkaKorpePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Kolicina")
    private int kolicina;
    @JoinColumn(name = "IdArt", referencedColumnName = "IdArt", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artikal artikal;
    @JoinColumn(name = "IdKorpa", referencedColumnName = "IdKorpa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Korpa korpa;

    public StavkaKorpe() {
    }

    public StavkaKorpe(StavkaKorpePK stavkaKorpePK) {
        this.stavkaKorpePK = stavkaKorpePK;
    }

    public StavkaKorpe(StavkaKorpePK stavkaKorpePK, int kolicina) {
        this.stavkaKorpePK = stavkaKorpePK;
        this.kolicina = kolicina;
    }

    public StavkaKorpe(int idKorpa, int idArt) {
        this.stavkaKorpePK = new StavkaKorpePK(idKorpa, idArt);
    }

    public StavkaKorpePK getStavkaKorpePK() {
        return stavkaKorpePK;
    }

    public void setStavkaKorpePK(StavkaKorpePK stavkaKorpePK) {
        this.stavkaKorpePK = stavkaKorpePK;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    public Korpa getKorpa() {
        return korpa;
    }

    public void setKorpa(Korpa korpa) {
        this.korpa = korpa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stavkaKorpePK != null ? stavkaKorpePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaKorpe)) {
            return false;
        }
        StavkaKorpe other = (StavkaKorpe) object;
        if ((this.stavkaKorpePK == null && other.stavkaKorpePK != null) || (this.stavkaKorpePK != null && !this.stavkaKorpePK.equals(other.stavkaKorpePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaKorpe[ stavkaKorpePK=" + stavkaKorpePK + " ]";
    }
    
}
