package com.julym.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class NsfwConfig {
    private final String filePath;
    public porn _porn;
    public sexy _sexy;
    public neutral _neutral;
    public hentai _hentai;
    public drawing _drawing;
    public groups _groups;
    public model _model;
    public master _master;
    public reply _reply;
    public whiteList _whiteList;


    public NsfwConfig(String filePath){
        this.filePath = filePath;
        this._groups = new groups();
        this._master = new master();
        this._whiteList = new whiteList();
    }

    public void LoadConfig(){
        Path path = Paths.get(filePath);
        byte[] data;
        try {
            data = Files.readAllBytes(path);
            JSONObject jsonConfig = JSONObject.parseObject(new String(data, StandardCharsets.UTF_8));
            this._porn = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("porn")), porn.class);
            this._sexy = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("sexy")), sexy.class);
            this._groups.group = JSONArray.parseArray(JSON.toJSONString(jsonConfig.get("groups")));
            this._neutral = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("neutral")), neutral.class);
            this._hentai = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("hentai")),hentai.class);
            this._drawing = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("drawing")), drawing.class);
            this._model = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("model")), model.class);
            this._master.setMaster(jsonConfig.get("master").toString());
            this._reply = JSONObject.parseObject(JSON.toJSONString(jsonConfig.get("reply")), reply.class);
            this._whiteList.lists = JSONArray.parseArray(JSON.toJSONString(jsonConfig.get("whiteList")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveConfig(){
        try {
            JSONObject jsonConfig = new JSONObject();
            jsonConfig.put("porn", _porn);
            jsonConfig.put("sexy", _sexy);
            jsonConfig.put("neutral", _neutral);
            jsonConfig.put("hentai", _hentai);
            jsonConfig.put("drawing", _drawing);
            jsonConfig.put("groups", _groups.group);
            jsonConfig.put("model", _model);
            jsonConfig.put("master", _master.getMaster());
            jsonConfig.put("reply", _reply);
            jsonConfig.put("whiteList", _whiteList.lists);
            File _JsonConfig = new File(filePath);
            if(_JsonConfig.createNewFile()) System.out.println("Create config.json file failed");
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(jsonConfig.toJSONString());
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class whiteList{

        private JSONArray lists;

        public void add(String num) {
            this.lists.add(num);
        }

        public void remove(String num){
            this.lists.remove(num);
        }

        public boolean isWhite(String num){
            for (Object o : this.lists) {
                String n = (String) o;
                if (n.equals(num)){
                    return true;
                }
            }
            return false;
        }

        public void clear(){
            this.lists.clear();
        }

        public String getAll(){
            return this.lists.toString();
        }

    }

    public static class master{

        private String master;

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

    }

    public static class model{
        @JSONField(name = "name")
        private String modelName;

        public model(String modelName){
            super();
            this.modelName = modelName;
        }
        public String getModelName() {
            return modelName;
        }

        /**
         * @param model 1 = inceptionv3 ; 2 = mobilenet ; default = resnet50
         */
        public void setModelName(int model) {
            switch (model){
                case 1:
                    this.modelName = "model.inceptionv3.onnx";
                    break;
                case 2:
                    this.modelName = "model.mobilenet.onnx";
                    break;
                default:
                    this.modelName = "model.resnet50.onnx";
                    break;
            }

        }
    }

    public static class groups{

        private JSONArray group;

        public void add(String num) {
            this.group.add(num);
        }

        public void remove(String num){
            this.group.remove(num);
        }

        public boolean hasGroup(String num){
            for (Object o : this.group) {
                String n = (String) o;
                if (n.equals(num)){
                    return true;
                }
            }
            return false;
        }

        public void clear(){
            this.group.clear();
        }

        public String getAll(){
            return this.group.toString();
        }

    }

    public static class reply{

        @JSONField(name = "mute")
        private String muteReply;
        @JSONField(name = "recall")
        private String recallReply;
        @JSONField(name = "nothing")
        private String nothingReply;
        @JSONField(name = "muteRecall")
        private String muteRecallReply;
        @JSONField(name = "admin")
        private String adminReply;
        @JSONField(name = "owner")
        private String ownerReply;
        @JSONField(name = "permission")
        private String perReply;

        public reply(String muteReply, String recallReply, String nothingReply, String muteRecallReply, String adminReply, String ownerReply, String perReply) {
            this.muteReply = muteReply;
            this.recallReply = recallReply;
            this.nothingReply = nothingReply;
            this.muteRecallReply = muteRecallReply;
            this.adminReply = adminReply;
            this.ownerReply = ownerReply;
            this.perReply = perReply;
        }

        public String getMuteReply() {
            return muteReply;
        }

        public void setMuteReply(String muteReply) {
            this.muteReply = muteReply;
        }

        public String getRecallReply() {
            return recallReply;
        }

        public void setRecallReply(String recallReply) {
            this.recallReply = recallReply;
        }

        public String getNothingReply() {
            return nothingReply;
        }

        public void setNothingReply(String nothingReply) {
            this.nothingReply = nothingReply;
        }

        public String getMuteRecallReply() {
            return muteRecallReply;
        }

        public void setMuteRecallReply(String muteRecallReply) {
            this.muteRecallReply = muteRecallReply;
        }

        public String getAdminReply() {
            return adminReply;
        }

        public void setAdminReply(String adminReply) {
            this.adminReply = adminReply;
        }

        public String getOwnerReply() {
            return ownerReply;
        }

        public void setOwnerReply(String ownerReply) {
            this.ownerReply = ownerReply;
        }

        public String getPerReply() {
            return perReply;
        }

        public void setPerReply(String perReply) {
            this.perReply = perReply;
        }
    }

    public static class porn{
        @JSONField(name = "threshold")
        private float threshold;
        @JSONField(name = "banType")
        private int banType;
        @JSONField(name = "banTime")
        private int banTime;

        public porn(float threshold, int banType, int banTime){
            super();
            this.threshold = threshold;
            this.banType = banType;
            this.banTime = banTime;
        }
        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public int getBanType() {
            return banType;
        }

        public void setBanType(int banType) {
            this.banType = banType;
        }

        public int getBanTime() {
            return banTime;
        }

        public void setBanTime(int banTime) {
            this.banTime = banTime;
        }
    }

    public static class sexy{
        @JSONField(name = "threshold")
        private float threshold;
        @JSONField(name = "banType")
        private int banType;
        @JSONField(name = "banTime")
        private int banTime;

        public sexy(float threshold, int banType, int banTime){
            super();
            this.threshold = threshold;
            this.banType = banType;
            this.banTime = banTime;
        }
        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public int getBanType() {
            return banType;
        }

        public void setBanType(int banType) {
            this.banType = banType;
        }

        public int getBanTime() {
            return banTime;
        }

        public void setBanTime(int banTime) {
            this.banTime = banTime;
        }
    }

    public static class neutral{
        @JSONField(name = "threshold")
        private float threshold;
        @JSONField(name = "banType")
        private int banType;
        @JSONField(name = "banTime")
        private int banTime;

        public neutral(float threshold, int banType, int banTime){
            super();
            this.threshold = threshold;
            this.banType = banType;
            this.banTime = banTime;
        }
        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public int getBanType() {
            return banType;
        }

        public void setBanType(int banType) {
            this.banType = banType;
        }

        public int getBanTime() {
            return banTime;
        }

        public void setBanTime(int banTime) {
            this.banTime = banTime;
        }
    }

    public static class hentai{
        @JSONField(name = "threshold")
        private float threshold;
        @JSONField(name = "banType")
        private int banType;
        @JSONField(name = "banTime")
        private int banTime;

        public hentai(float threshold, int banType, int banTime){
            super();
            this.threshold = threshold;
            this.banType = banType;
            this.banTime = banTime;
        }
        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public int getBanType() {
            return banType;
        }

        public void setBanType(int banType) {
            this.banType = banType;
        }

        public int getBanTime() {
            return banTime;
        }

        public void setBanTime(int banTime) {
            this.banTime = banTime;
        }
    }

    public static class drawing{
        @JSONField(name = "threshold")
        private float threshold;
        @JSONField(name = "banType")
        private int banType;
        @JSONField(name = "banTime")
        private int banTime;

        public drawing(float threshold, int banType, int banTime){
            super();
            this.threshold = threshold;
            this.banType = banType;
            this.banTime = banTime;
        }
        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public int getBanType() {
            return banType;
        }

        public void setBanType(int banType) {
            this.banType = banType;
        }

        public int getBanTime() {
            return banTime;
        }

        public void setBanTime(int banTime) {
            this.banTime = banTime;
        }
    }
}
