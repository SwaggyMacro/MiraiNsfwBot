package com.julym.bot;

import com.alibaba.fastjson.JSONArray;
import com.julym.Nsfw;
import com.julym.config.NsfwConfig;
import com.julym.util.AntiModel;
import com.julym.util.ImageUtil;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.julym.util.utils;

import javax.imageio.ImageIO;

public class BotRunner {
    private final String pluginPath;
    public String Master;
    private final NsfwConfig nsfwConfig;
    private AntiModel antiModel;

    public BotRunner(){
        this.pluginPath = utils.getJavaRunPath() + "plugins" + File.separator + "NSFW" + File.separator;
        String pluginImgPath = this.pluginPath + "images" + File.separator;
        String configPath = this.pluginPath + "config.json";
        File file = new File(this.pluginPath);
        if (!file.exists()){
            if (file.mkdirs()){
                LogInfo("Create folder NSFW Successfully.");
            } else {
                LogInfo("Create folder NSFW Failed.");
            }
        }
        file = new File(pluginImgPath);
        if (!file.exists()){
            if (file.mkdirs()){
                LogInfo("Create folder images Successfully.");
            } else {
                LogInfo("Create folder images Failed.");
            }
        }
        file = new File(configPath);
        if (!file.exists()){
            utils.generateConfig(configPath);
        }
        nsfwConfig = new NsfwConfig(configPath);
        nsfwConfig.LoadConfig();
        this.Master = nsfwConfig._master.getMaster();
        antiModel = new AntiModel("model.resnet50.onnx");
        LogInfo("CNN Model Loaded.");
    }

    private void LogInfo(String logMsg){
        Nsfw.getInstance().getLogger().info(logMsg);
    }

    private void SendMessage(GroupMessageEvent event, String context){
        MessageChain chain = new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append(context)
                .build();
        event.getSubject().sendMessage(chain);
    }

    private void setMaster(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW????????????:","");
        nsfwConfig._master.setMaster(context);
        this.Master = context;
        nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void setThreshold(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW????????????:","");
        String _Class = context.trim().split("-")[0];
        float _Threshold = Float.parseFloat(context.trim().split("-")[1]);
        switch (_Class){
            case "sexy":
                this.nsfwConfig._sexy.setThreshold(_Threshold);
                break;
            case "porn":
                this.nsfwConfig._porn.setThreshold(_Threshold);
                break;
            case "hentai":
                this.nsfwConfig._hentai.setThreshold(_Threshold);
                break;
        }
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void addGroup(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW?????????:","");
        this.nsfwConfig._groups.add(context);
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void delGroup(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW?????????:","");
        this.nsfwConfig._groups.remove(context);
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void addToWhitelist(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW???????????????:","");
        this.nsfwConfig._whiteList.add(context);
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void delFromWhitelist(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW???????????????:","");
        this.nsfwConfig._whiteList.remove(context);
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    /**
     * Switch Model
     * @param event GroupMessageEvent
     * @param modelNum  model 1 = inceptionv3 ; 2 = mobilenet ; default = resnet50
     */
    private void switchModel(GroupMessageEvent event, int modelNum){
        this.antiModel = null;
        System.gc();
        this.nsfwConfig._model.setModelName(modelNum);
        this.nsfwConfig.SaveConfig();
        this.antiModel = new AntiModel(this.nsfwConfig._model.getModelName());
        SendMessage(event, "????????????!");
    }

    private void setBanType(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW????????????:","");
        String _Class = context.trim().split("-")[0];
        int _BanType = Integer.parseInt(context.trim().split("-")[1]);
        switch (_Class){
            case "sexy":
                this.nsfwConfig._sexy.setBanType(_BanType);
                break;
            case "porn":
                this.nsfwConfig._porn.setBanType(_BanType);
                break;
            case "hentai":
                this.nsfwConfig._hentai.setBanType(_BanType);
                break;
        }
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void setBanTime(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW??????????????????:","");
        String _Class = context.trim().split("-")[0];
        int _BanTime = Integer.parseInt(context.trim().split("-")[1]);
        switch (_Class){
            case "sexy":
                this.nsfwConfig._sexy.setBanTime(_BanTime);
                break;
            case "porn":
                this.nsfwConfig._porn.setBanTime(_BanTime);
                break;
            case "hentai":
                this.nsfwConfig._hentai.setBanTime(_BanTime);
                break;
        }
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    private void setReply(GroupMessageEvent event, String context){
        context = context.replace("???",":").replace("!NSFW????????????:","");
        String _Class = context.trim().split("-")[0];
        String reply = context.trim().split("-")[1];
        switch (_Class){
            case "??????":
                this.nsfwConfig._reply.setMuteReply(reply);
                break;
            case "??????":
                this.nsfwConfig._reply.setRecallReply(reply);
                break;
            case "????????????":
            case "????????????":
                this.nsfwConfig._reply.setMuteRecallReply(reply);
                break;
            case "??????":
                this.nsfwConfig._reply.setOwnerReply(reply);
                break;
            case "?????????":
                this.nsfwConfig._reply.setAdminReply(reply);
                break;
            case "?????????":
                this.nsfwConfig._reply.setNothingReply(reply);
                break;
            case "??????":
                this.nsfwConfig._reply.setPerReply(reply);
                break;
        }
        this.nsfwConfig.SaveConfig();
        SendMessage(event, "????????????!");
    }

    public void commandManager(GroupMessageEvent event, MessageChain chain){
        if (chain.contentToString().replace("???",":").indexOf("!NSFW????????????:") == 0) {
            if (this.Master.equals("")){
                this.setMaster(event, chain.contentToString());
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.setMaster(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }

        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.setReply(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.setThreshold(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }

        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                String modelNum = chain.contentToString().replace("???",":").trim().replace("!NSFW????????????:", "");
                this.switchModel(event,Integer.parseInt(modelNum));
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }

        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW???????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.addToWhitelist(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW???????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.delFromWhitelist(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW?????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.addGroup(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW?????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.delGroup(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.setBanType(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }
        if (chain.contentToString().replace("???",":").indexOf("!NSFW??????????????????:") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.setBanTime(event, chain.contentToString());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW?????????????????????") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                event.getSubject().sendMessage(new MessageChainBuilder().
                        append(new At(event.getSender().getId())).
                        append(this.nsfwConfig._whiteList.getAll()).
                        build());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW???????????????") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                event.getSubject().sendMessage(new MessageChainBuilder().
                        append(new At(event.getSender().getId())).
                        append(this.nsfwConfig._groups.getAll()).
                        build());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW???????????????") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.nsfwConfig._groups.clear();
                this.nsfwConfig.SaveConfig();
                event.getSubject().sendMessage(new MessageChainBuilder().
                        append(new At(event.getSender().getId())).
                        append("????????????!").
                        build());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }

        if (chain.contentToString().replace("???",":").indexOf("!NSFW???????????????") == 0) {
            if (this.Master.equals("")){
                SendMessage(event, "???????????? \"!NSFW????????????:QQ???\" ?????????????????????QQ??? ");
            }else if(this.Master.equals(Long.toString(event.getSender().getId()))){
                this.nsfwConfig._whiteList.clear();
                this.nsfwConfig.SaveConfig();
                event.getSubject().sendMessage(new MessageChainBuilder().
                        append(new At(event.getSender().getId())).
                        append("????????????!").
                        build());
            }else{
                SendMessage(event, "???????????????????????????????????????");
            }
        }
    }

    public void receiveMessage(GroupMessageEvent event){

        MessageChain chain = event.getMessage();

        if (chain.contentToString().indexOf("!NSFW") == 0) {
            commandManager(event, chain);
        }

        if (this.nsfwConfig._groups.hasGroup(String.valueOf(event.getSubject().getId())) &&
                !this.nsfwConfig._whiteList.isWhite(String.valueOf(event.getSender().getId()))){


            if (chain.contentToString().equals("SwaggyMacro")){
                SendMessage(event, "??????, ?????????????????????????????????????????????");
            }
            String msg = event.getSource().getOriginalMessage().toString();
            String pattern = "\\[mirai:(image|flash):(.*?)\\.(.*?)\\]";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(msg);
            while(m.find()) {
                Image image = Image.fromId(m.group(2) + '.' + m.group(3));
                try {
                    String imgPath = ImageUtil.saveImage(image, this.pluginPath);
                    BufferedImage bufferedImage = ImageIO.read(new FileInputStream(imgPath));
                    String ret = antiModel.detector(bufferedImage);
                    LogInfo(ret);
                    JSONArray json = JSONArray.parseArray(ret);
                    String TopClass = json.getJSONObject(0).getString("className");
                    String TopProbability = json.getJSONObject(0).getString("probability");
                    MessageChain messages;

                    if (TopClass.equals("porn") || TopClass.equals("sexy") || TopClass.equals("hentai")) {
                        if (Float.parseFloat(TopProbability) < this.nsfwConfig._porn.getThreshold()){
                            break;
                        }
                        if (TopClass.equals("porn")){
                            if (Float.parseFloat(TopProbability) < this.nsfwConfig._porn.getThreshold()){
                                break;
                            }
                        }
                        if (TopClass.equals("sexy")){
                            if (Float.parseFloat(TopProbability) < this.nsfwConfig._sexy.getThreshold()){
                                break;
                            }
                        }
                        if (TopClass.equals("hentai")){
                            if (Float.parseFloat(TopProbability) < this.nsfwConfig._hentai.getThreshold()){
                                break;
                            }
                        }
                        if (event.getSource().getSender().getPermission() == MemberPermission.ADMINISTRATOR){
                            messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getAdminReply()).build();
                        } else if (event.getSource().getSender().getPermission() == MemberPermission.OWNER){
                            messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getOwnerReply()).build();
                        } else {
                            if (event.getGroup().getBotPermission() == MemberPermission.ADMINISTRATOR || event.getGroup().getBotPermission() == MemberPermission.OWNER) {
                                if (this.nsfwConfig._porn.getBanType() == 2) { // 0=nothing 1=mute 2=recall 3=mute and recall
                                    MessageSource.recall(chain);
                                    messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getRecallReply()).build();
                                } else if (this.nsfwConfig._porn.getBanType() == 3) {
                                    MessageSource.recall(chain);
                                    event.getSender().mute(this.nsfwConfig._porn.getBanTime());
                                    messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getMuteRecallReply()).build();
                                } else if (this.nsfwConfig._porn.getBanType() == 1) {
                                    event.getSender().mute(this.nsfwConfig._porn.getBanTime());
                                    messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getMuteReply()).build();
                                } else {
                                    messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getNothingReply()).build();
                                }
                            } else {
                                messages = new MessageChainBuilder().append(new At(event.getSender().getId())).append(this.nsfwConfig._reply.getPerReply()).build();
                            }
                        }
                        event.getSubject().sendMessage(messages);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
