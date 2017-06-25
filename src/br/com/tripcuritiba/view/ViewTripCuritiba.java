package br.com.tripcuritiba.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import br.com.tripcuritiba.controller.TripCuritibaController;
import br.com.tripcuritiba.dto.TripCuritibaDTO;
import br.com.tripcuritiba.jasper.JasperFactory;
import br.com.tripcuritiba.model.Pessoa;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ViewTripCuritiba
{

    private static Scanner entrada;

	public static void main(String[] args) throws JRException
    {
		
		reportBuilder();
		getP_Email();

        while (continuar() == true)
        {
        	getP_Email();
        }
    }

    private static boolean continuar()
    {
        System.out.println("Deseja enviar outro levantamento? S/N");
        Scanner sc = new Scanner(System.in);
        String opcao = sc.next();
        if(opcao.equals("S")||opcao.equals("s")){
        	return true;
        }else{
        	return false;
        }      
    }    
    
    private static void getP_Email(){
    	String senha;    	
    	String remetente;	
    	String destinatario;
    	entrada = new Scanner (System.in);
    	System.out.println("Digite a senha do email utilizado:");    	
    	
    	do{    	        	
            senha = entrada.next();
        }while((senha.equals(""))&(senha.equals(" ")));
        
        System.out.println("Digite o nome do remetente:");
        do{    	        	
            remetente = entrada.next();
        }while(remetente.equals("")&remetente.equals(" "));
        
        System.out.println("Digite o email do destinatário:");
        do{    	        	
            destinatario = entrada.next();
        }while(destinatario.equals("")&destinatario.equals(" "));
               
        try
        {
        	enviarEmail(senha, remetente, destinatario);
        } catch (Exception e) {
        	System.out.println("Falha ao enviar email!!!");
        	getP_Email();
		}
      
    }
    private static void enviarEmail(String pSenha, String pRemetente, String pDestinatario) throws AddressException, MessagingException{
    	String tServidor = "smtp.gmail.com";
        String tPorta = "465";
        String tUsuario = "wellclbo@gmail.com";
        String tOrigem = "wellclbo@gmail.com";

        Properties tPropriedades = new Properties();
        tPropriedades.put("mail.smtp.host", tServidor);
        tPropriedades.put("mail.smtp.port", tPorta);
        tPropriedades.put("mail.smtp.user", tUsuario);
        tPropriedades.put("mail.smtp.auth", "true");
        tPropriedades.put("mail.smtp.debug", "true");
        tPropriedades.put("mail.smtp.socketFactory.port", tPorta);
        tPropriedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        tPropriedades.put("mail.smtp.socketFactory.fallback", "false");

        // Obtendo o objeto Session
        Session tSessao = Session.getInstance(tPropriedades, new Autenticador(tUsuario, pSenha));


            // Criando a mensagem
            Message tMensagem = new MimeMessage(tSessao);

            // Configurando a mensagem.
            tMensagem.setFrom(new InternetAddress(tOrigem));
            tMensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pDestinatario));
            tMensagem.setSubject("Relatório das Pessoas cadastradas");

            // Criando o e-mail com varias partes
            Multipart tMultiplasPartes = new MimeMultipart();

            // Criando a parte texto do e-mail e armazenando
            BodyPart tParteTexto = new MimeBodyPart();
            tParteTexto.setText("Olá " + pDestinatario + ", em anexo segue relatório solicitado");
            tMultiplasPartes.addBodyPart(tParteTexto);

            // Criando a parte que contÃ©m o arquivo em anexo
            BodyPart tParteArquivo = new MimeBodyPart();
            String tNomeArquivo = "./jasperoutput/RelatorioPessoas.pdf";
            //String tNomeArquivo = "carro.jpg";
            DataSource tArquivo = new FileDataSource(tNomeArquivo);
            tParteArquivo.setDataHandler(new DataHandler(tArquivo));
            tParteArquivo.setFileName(tNomeArquivo);
            tMultiplasPartes.addBodyPart(tParteArquivo);

            // Colocando na mesangem as partes
            tMensagem.setContent(tMultiplasPartes);

            // Enviando a mensagem
            Transport.send(tMensagem);
    }

    private static void reportBuilder() throws JRException{
    	JasperReport jasperReport = JasperFactory.getRelatorioPessoas();

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();
        

        // Obtendo a lista de Pessoas
        TripCuritibaDTO tDto = TripCuritibaController.pesquisar();
        if (tDto.isOk())
        {
            List<Pessoa> tLista = tDto.getLista();

            // DataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(tLista);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                            parameters, dataSource);

            // Make sure the output directory exists.
            File outDir = new File("./jasperoutput");
            outDir.mkdirs();

            // Export to PDF.
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                            "./jasperoutput/RelatorioPessoas.pdf");
        }
    }

    private static class Autenticador extends Authenticator
    {
        private String mUsuario;
        private String mSenha;

        public Autenticador(String pUsuario, String pSenha)
        {
            super();
            mUsuario = pUsuario;
            mSenha = pSenha;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(mUsuario, mSenha);
        }
    }
}
