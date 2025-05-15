package com.eldermoraes;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;

@Path("pessoa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PessoaResource {


    @GET
    @Counted(value = "counted.getPessoa")
    public List<Pessoa> getPessoa() {
        return Pessoa.listAll();
    }

    @GET
    @Path("findByAnoNascimento")
    public List<Pessoa> findByAnoNascimento(@QueryParam("anoNascimento") int anoNascimento) {
        return Pessoa.findByAnoNascimento(anoNascimento);
    }


    @POST
    @Transactional
    public Pessoa createPessoa(Pessoa pessoa) {
        pessoa.id = null;
        pessoa.persist();

        return pessoa;
    }

    @PUT
    @Transactional
    public Pessoa updatePessoa(Pessoa pessoa) {
        Pessoa p = Pessoa.findById(pessoa.id);
        p.nome = pessoa.nome;
        p.anoNascimento = pessoa.anoNascimento;
        p.persist();

        return p;
    }

    @DELETE
    @Transactional
    public void deletePessoa(int id) {
        Pessoa.deleteById(id);
    }
}




