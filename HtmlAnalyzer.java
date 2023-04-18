import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        String url = args[0];
        
        try {
            URL website = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
            
            //String para armazenar o texto enquanto percorre o resto do arquivo
            String deeperer="";

            //int para contar e comparar a quantidade de tags
            int count=0;

            //REGEX para encontrar tags
            Pattern openTagPattern = Pattern.compile("<[A-Za-z][A-Za-z0-9]*>");
            Matcher matcher;
            Pattern closeTagPattern = Pattern.compile(("<\\/[A-Za-z][A-Za-z0-9]*>"));
            String inputLine;

            //lista de tags para verificar malformed
            List<String> tagList = new ArrayList<String>();

            //lendo linha e descobrindo se tag de abertura, tag fechamento ou texto.
            while ((inputLine = in.readLine()) != null) {
                
                //removendo espacos vazios
                inputLine=inputLine.replaceAll("(?m)^[\\s&&[^\\n]]+|^[\n]", "");
                matcher = openTagPattern.matcher(inputLine);
                if(matcher.find()) {
                    tagList.add(inputLine);
                }
                else{   
                    matcher = closeTagPattern.matcher(inputLine);
                    
                    if(matcher.find()) {
                        //removendo / da tag de fechamento para realizar comparacao com a ultima tag inserida na lista
                        inputLine=inputLine.replaceAll("/","");
                        if(inputLine.equals(tagList.get(tagList.size()-1))) {
                            tagList.remove(tagList.size()-1);
                        }
                        else{
                            deeperer="malformed HTML";
                            break;
                        }
                    }
                    //se o texto estiver mais profundo que o anterior

                    else if(tagList.size()>count){
                        count=tagList.size();
                        deeperer=inputLine;
                    }
                }
            }
            in.close();

            if(tagList.size()!=0 || deeperer==""){
                deeperer="malformed HTML";
            }

            System.out.println(deeperer);

        } 
         catch (Exception e) {
            System.out.println("URL connection error");
       }
    }
}