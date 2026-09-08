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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "korpa")
@NamedQueries({
    @NamedQuery(name = "Korpa.findAll", query = "SELECT k FROM Korpa k")})
public class Korpa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdKorpa")
    private Integer idKorpa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UkupnaCena")
    private BigDecimal ukupnaCena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korpa")
    private List<StavkaKorpe> stavkaKorpeList;

    public Korpa() {
    }

    public Korpa(Integer idKorpa) {
        this.idKorpa = idKorpa;
    }

    public Korpa(Integer idKorpa, int idKor) {
        this.idKorpa = idKorpa;
        this.idKor = idKor;
    }

    public Integer getIdKorpa() {
        return idKorpa;
    }

    public void setIdKorpa(Integer idKorpa) {
        this.idKorpa = idKorpa;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
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
        hash += (idKorpa != null ? idKorpa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korpa)) {
            return false;
        }
        Korpa other = (Korpa) object;
        if ((this.idKorpa == null && other.idKorpa != null) || (this.idKorpa != null && !this.idKorpa.equals(other.idKorpa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Korpa[ idKorpa=" + idKorpa + " ]";
    }
    
}
