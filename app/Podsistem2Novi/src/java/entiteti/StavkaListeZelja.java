/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "stavka_liste_zelja")
@NamedQueries({
    @NamedQuery(name = "StavkaListeZelja.findAll", query = "SELECT s FROM StavkaListeZelja s")})
public class StavkaListeZelja implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StavkaListeZeljaPK stavkaListeZeljaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VremeDodavanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeDodavanja;
    @JoinColumn(name = "IdArt", referencedColumnName = "IdArt", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artikal artikal;
    @JoinColumn(name = "IdListe", referencedColumnName = "IdListe", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ListaZelja listaZelja;

    public StavkaListeZelja() {
    }

    public StavkaListeZelja(StavkaListeZeljaPK stavkaListeZeljaPK) {
        this.stavkaListeZeljaPK = stavkaListeZeljaPK;
    }

    public StavkaListeZelja(StavkaListeZeljaPK stavkaListeZeljaPK, Date vremeDodavanja) {
        this.stavkaListeZeljaPK = stavkaListeZeljaPK;
        this.vremeDodavanja = vremeDodavanja;
    }

    public StavkaListeZelja(int idListe, int idArt) {
        this.stavkaListeZeljaPK = new StavkaListeZeljaPK(idListe, idArt);
    }

    public StavkaListeZeljaPK getStavkaListeZeljaPK() {
        return stavkaListeZeljaPK;
    }

    public void setStavkaListeZeljaPK(StavkaListeZeljaPK stavkaListeZeljaPK) {
        this.stavkaListeZeljaPK = stavkaListeZeljaPK;
    }

    public Date getVremeDodavanja() {
        return vremeDodavanja;
    }

    public void setVremeDodavanja(Date vremeDodavanja) {
        this.vremeDodavanja = vremeDodavanja;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    public ListaZelja getListaZelja() {
        return listaZelja;
    }

    public void setListaZelja(ListaZelja listaZelja) {
        this.listaZelja = listaZelja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stavkaListeZeljaPK != null ? stavkaListeZeljaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaListeZelja)) {
            return false;
        }
        StavkaListeZelja other = (StavkaListeZelja) object;
        if ((this.stavkaListeZeljaPK == null && other.stavkaListeZeljaPK != null) || (this.stavkaListeZeljaPK != null && !this.stavkaListeZeljaPK.equals(other.stavkaListeZeljaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaListeZelja[ stavkaListeZeljaPK=" + stavkaListeZeljaPK + " ]";
    }
    
}
