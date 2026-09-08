/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "lista_zelja")
@NamedQueries({
    @NamedQuery(name = "ListaZelja.findAll", query = "SELECT l FROM ListaZelja l")})
public class ListaZelja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdListe")
    private Integer idListe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VremeKreiranjaListe")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeKreiranjaListe;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listaZelja")
    private List<StavkaListeZelja> stavkaListeZeljaList;

    public ListaZelja() {
    }

    public ListaZelja(Integer idListe) {
        this.idListe = idListe;
    }

    public ListaZelja(Integer idListe, int idKor, Date vremeKreiranjaListe) {
        this.idListe = idListe;
        this.idKor = idKor;
        this.vremeKreiranjaListe = vremeKreiranjaListe;
    }

    public Integer getIdListe() {
        return idListe;
    }

    public void setIdListe(Integer idListe) {
        this.idListe = idListe;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public Date getVremeKreiranjaListe() {
        return vremeKreiranjaListe;
    }

    public void setVremeKreiranjaListe(Date vremeKreiranjaListe) {
        this.vremeKreiranjaListe = vremeKreiranjaListe;
    }

    public List<StavkaListeZelja> getStavkaListeZeljaList() {
        return stavkaListeZeljaList;
    }

    public void setStavkaListeZeljaList(List<StavkaListeZelja> stavkaListeZeljaList) {
        this.stavkaListeZeljaList = stavkaListeZeljaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idListe != null ? idListe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaZelja)) {
            return false;
        }
        ListaZelja other = (ListaZelja) object;
        if ((this.idListe == null && other.idListe != null) || (this.idListe != null && !this.idListe.equals(other.idListe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.ListaZelja[ idListe=" + idListe + " ]";
    }
    
}
