package br.fatec.TemosVagas.services;

import br.fatec.TemosVagas.entities.enums.StatusAplicacao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private void enviarEmailTemplate(String destinatario, String assunto, String nomeTemplate, Context context) throws MessagingException {

        String conteudoHtml = templateEngine.process(nomeTemplate, context);

        MimeMessage mensagem = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

        helper.setFrom("Temos Vagas <vagastemos72@gmail.com>");
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(conteudoHtml, true);

        emailSender.send(mensagem);
    }

    @Async
    public void enviarEmailCadastro(String destinatario, String nome) throws MessagingException {
        String assunto = "Bem vindo(a) à Temos Vagas";

        Context context = new Context();
        context.setVariable("nome", nome);
        enviarEmailTemplate(destinatario, assunto, "templateEmailCadastro",context);
    }

    @Async
    public void enviarEmailRedefinirSenha(String destinatario, String nome, String token) throws MessagingException {
        String assunto = "Redefinição de Senha";

        Context context = new Context();
        context.setVariable("nome", nome);
        context.setVariable("token", token);
        enviarEmailTemplate(destinatario, assunto, "templateEmailRedefinirSenha",context);
    }


    //TODO: Emails referente as vagas

    @Async
    public void enviarEmailAttStatus(String destinatario, String nome, String nomeVaga, String nomeEmpresa,
                                     StatusAplicacao status) throws MessagingException {
        String assunto = "Atualização em sua Candidatura - " + nomeVaga;

        Context context = new Context();
        context.setVariable("nome", nome);
        context.setVariable("nomeVaga", nomeVaga);
        context.setVariable("nomeEmpresa", nomeEmpresa);
        context.setVariable("status", status.toString());

        enviarEmailTemplate(destinatario, assunto, "templateEmailAtualizacaoStatus",context);
    }
}
