package com.example.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration  // Adicione a anotação @Configuration para registrar essa classe como uma configuração
public class WebConfig implements WebMvcConfigurer {


   // @Override
   // public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
   //     if (isDevelopment()) {
   //         System.out.println("isDevelopment");
   //         registry.addMapping("/*")
   //                 .allowedMethods("GET", "POST", "PUT", "DELETE")
   //                 .allowedOrigins("*")
   //                 .allowedHeaders("Authorization", "Content-Type")
   //                 .allowCredentials(true);
   //     } else {
   //         System.out.println("not's in isDevelopment");
   //         // Em produção, restringe a origem
   //         registry.addMapping("/**")
   //                 .allowedMethods("GET", "POST", "PUT", "DELETE")
   //                 .allowedOrigins("https://meu-frontend.com")
   //                 .allowedHeaders("Authorization", "Content-Type")
   //                 .allowCredentials(true);
   //     }
   // }

    private boolean isDevelopment() {
        try {
            // Obtém o endereço IP local
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("IP local: " + ip);  // Exibe o IP no console

            // Verifique se o IP é local (geralmente 127.0.0.1 ou algum IP na rede local)
            if (ip.startsWith("127.") || ip.startsWith("192.") || ip.startsWith("10.")) {
                System.out.println("Ambiente de desenvolvimento detectado");
                return true;  // Considera como ambiente de desenvolvimento
            }

            // Caso contrário, considera como ambiente de produção
            System.out.println("Ambiente de produção detectado");
            return false;

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

}
