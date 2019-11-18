package com.company;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HttpServer implements Server,Runnable {

    protected Socket clientDialog;
    protected OutputStream os;
    protected InputStream is;
    protected List<String> reqArr=new ArrayList<>();
    protected String method;
    protected String webDir="C:\\Users\\kolya\\KSPKAB-\\src\\com\\company";

    public HttpServer(Socket client) throws Throwable{
        this.clientDialog=client;
        this.is=client.getInputStream();
        this.os=client.getOutputStream();

    }

    private byte[] getFile(String fname){
        try {
            String[] q=fname.split("\\?");
            fname=q[0];
            String[] sre=fname.split("\\.");
            String ext=sre[sre.length-1];
            try {

                if (ext.equals("jar")) {
                    HashMap<String,String> hs;
                    HashMap<String, HashMap<String, String>> Request=null;
                    if(this.method.equalsIgnoreCase("GET")) {
                        hs = this.parseGetParam();
                        Request = new HashMap<>();
                        Request.put(this.method, hs);
                    }
                    else if(this.method.equalsIgnoreCase("POST")){
                        hs = this.parsePostParam();
                        Request = new HashMap<>();
                        Request.put(this.method, hs);
                    }

                    return this.runScript(webDir + "\\" + fname, Request);
                }
                else {
                    Path f = Paths.get(webDir + "\\"+fname);
                    if(Files.exists(f))
                        return Files.readAllBytes(f);
                    else
                        return Files.readAllBytes(Paths.get(webDir+"\\index.html"));
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
                return null;
            }


        }
        catch (Throwable t){
            t.printStackTrace();
            return null;
        }


    }

    private byte[] runScript(String pathToExecute,HashMap<String,HashMap<String,String>> request){
        Runtime runtime=Runtime.getRuntime();
        try {


            Process process=runtime.exec("java -jar "+pathToExecute);
            BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()));
            ObjectOutputStream oos=new ObjectOutputStream(process.getOutputStream());
            oos.writeObject(request);
            oos.flush();
            String s="";
            while (process.isAlive());
            while (br.ready())
                s+=br.readLine();
            process.destroy();

            return s.getBytes();
        }
        catch (Throwable ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() {
        try {

            this.readData();
            byte[] wr=null;

                String[] parasm = reqArr.get(0).split("/");
                this.method=parasm[0].trim();
               // if(!this.method.equalsIgnoreCase("GET"))
                  //  return;
                String[] rm=parasm[1].split(" ");
                wr=this.getFile(rm[0]);
                this.writeResponse(wr);
                this.clientDialog.close();
        }
        catch (Throwable ex){
            ex.printStackTrace();
        }
        finally {
            try {
                this.clientDialog.close();
            } catch (Throwable t) {
                /*do nothing*/
            }
        }

    }

    private HashMap<String,String> parseGetParam() throws Throwable {

             HashMap<String,String> hs=new HashMap<>();

                String[] parasm= reqArr.get(0).split("/");
                String[] rm=parasm[1].split(" ");
                String[] qarr=rm[0].split("\\?");
                String[] param=qarr[1].split("&");
                for (String st:param) {
                    String[] kv=st.split("=");
                    hs.put(kv[0],kv[1]);

                }

                return hs;

    }

    private HashMap<String,String> parsePostParam(){
        HashMap<String,String> hs=new HashMap<>();
        String[] param=reqArr.get(reqArr.size()-1).split("&");
        for (String st:param) {
            String[] kv=st.split("=");
            hs.put(kv[0],kv[1]);

        }

        return hs;
    }

    private void readData() throws Throwable {
        //InputStreamReader ins=new InputStreamReader(is);


            //FuckJavadevelopers br = new FuckJavadevelopers(ins);


        List<String> res=new ArrayList<>();
        byte[] bytes=new byte[26000];
        is.read(bytes);
        String s=new String(bytes,StandardCharsets.UTF_8);
        res=Arrays.asList(s.split("\n"));
        System.out.println(s);
        this.reqArr=res;

        return;
    }

    private void writeResponse(byte[] s) throws Throwable {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + s.length + "\r\n" +
                "Connection: close\r\n\r\n";
        String x=new String(s);

        response+=x;
        /*
        byte[] result=new byte[s.length+response.getBytes().length];
        for (int i=0;i<response.getBytes().length;i++) {
            result[i]=response.getBytes()[i];
        }
        for (int i=0;i<s.length;i++) {
            result[i+response.getBytes().length]=s[i];
        }*/

        Writer ow=new OutputStreamWriter(os,"UTF-8");
        ow.write(response);
        ow.flush();
    }

}
