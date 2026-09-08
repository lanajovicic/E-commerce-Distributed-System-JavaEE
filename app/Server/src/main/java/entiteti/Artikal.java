/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User
 */
@Entity
@Table(name = "artikal")
@NamedQueries({
    @NamedQuery(name = "Artikal.findAll", query = "SELECT a FROM Artikal a")})
public class Artikal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdArt")
    private Integer idArt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Lob
    @Size(max = 65535)
    @Column(name = "Opis")
    private String opis;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cena")
    private BigDecimal cena;
    @Column(name = "ProcenatPopusta")
    private BigDecimal procenatPopusta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdPro")
    private int idPro;
    @JoinColumn(name = "IdKat", referencedColumnName = "IdKat")
    @ManyToOne(optional = false)
    private Kategorija idKat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artikal")
    private List<StavkaListeZelja> stavkaListeZeljaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artikal")
    private List<StavkaKorpe> stavkaKorpeList;

    public Artikal() {
    }

    public Artikal(Integer idArt) {
        this.idArt = idArt;
    }

    public Artikal(Integer idArt, String naziv, BigDecimal cena, int idPro) {
        this.idArt = idArt;
        this.naziv = naziv;
        this.cena = cena;
        this.idPro = idPro;
    }

    public Integer getIdArt() {
        return idArt;
    }

    public void setIdArt(Integer idArt) {
        this.idArt = idArt;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public BigDecimal getProcenatPopusta() {
        return procenatPopusta;
    }

    public void setProcenatPopusta(BigDecimal procenatPopusta) {
        this.procenatPopusta = procenatPopusta;
    }

    public int getIdPro() {
        return idPro;
    }

    public void setIdPro(int idPro) {
        this.idPro = idPro;
    }

    public Kategorija getIdKat() {
        return idKat;
    }

    public void setIdKat(Kategorija idKat) {
        this.idKat = idKat;
    }

    public List<StavkaListeZelja> getStavkaListeZeljaList() {
        return stavkaListeZeljaList;
    }

    public void setStavkaListeZeljaList(List<StavkaListeZelja> stavkaListeZeljaList) {
        this.stavkaListeZeljaList = stavkaListeZeljaList;
    }

    public List<StavkaKorpe> getStavkaKorpeList() {
        return stavkaKorpeList;
    }

    public void setStavkaKorpeList(List<StavkaKorpe> stavkaKorpeList) {
        this.stavkaKorpeList = stavkaKorpeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArt != null ? idArt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikal)) {
            return false;
        }
        Artikal other = (Artikal) object;
        if ((this.idArt == null && other.idArt != null) || (this.idArt != null && !this.idArt.equals(other.idArt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Artikal[ idArt=" + idArt + " ]";
    }
    
}
