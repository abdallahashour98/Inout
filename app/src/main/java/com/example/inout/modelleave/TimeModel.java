package com.example.inout.modelleave;

public class TimeModel {
      private   String  day;
      private   String  month;
      private   String  year;
      private String id;
      private String user_id;


      public TimeModel() {
      }

      public TimeModel(String day, String month, String year, String id, String user_id) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.id = id;
            this.user_id = user_id;
      }

      public String getDay() {
            return day;
      }

      public void setDay(String day) {
            this.day = day;
      }

      public String getMonth() {
            return month;
      }

      public void setMonth(String month) {
            this.month = month;
      }

      public String getYear() {
            return year;
      }

      public void setYear(String year) {
            this.year = year;
      }

      public String getId() {
            return id;
      }

      public void setId(String id) {
            this.id = id;
      }

      public String getUser_id() {
            return user_id;
      }

      public void setUser_id(String user_id) {
            this.user_id = user_id;
      }
}
