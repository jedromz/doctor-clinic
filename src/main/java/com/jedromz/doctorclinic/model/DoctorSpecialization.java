package com.jedromz.doctorclinic.model;

public enum DoctorSpecialization {
    CARDIOLOGIST("cardiologist"), LARYNGOLOGIST("laryngologist"), ORTHOPAEDIST("orthopaedist"), DENTIST("dentist");
    private String specialization;

    DoctorSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
