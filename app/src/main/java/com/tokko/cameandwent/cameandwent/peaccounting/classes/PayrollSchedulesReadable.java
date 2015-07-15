package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-schedules-readable">
 *   &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *     &lt;!-- Reference to inner class Choice -->
 *   &lt;/xs:choice>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollSchedulesReadable{
    private List<Choice> choiceList = new ArrayList<Choice>();

    /**
     * Get the list of 'payroll-schedules-readable' complexType items.
     *
     * @return list
     */
    public List<Choice> getChoiceList(){
        return choiceList;
    }

    /**
     * Set the list of 'payroll-schedules-readable' complexType items.
     *
     * @param list
     */
    public void setChoiceList(List<Choice> list){
        choiceList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:choice xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0"
     * maxOccurs="unbounded">
     *   &lt;xs:element type="payroll-rolling-schedule-readable" name="rolling-schedule"/>
     *   &lt;xs:element type="payroll-fixed-schedule-readable" name="fixed-schedule"/>
     * &lt;/xs:choice>
     * </pre>
     */
    public static class Choice{
        private static final int ROLLING_SCHEDULE_CHOICE = 0;
        private static final int FIXED_SCHEDULE_CHOICE = 1;
        private int choiceListSelect = -1;
        private PayrollRollingScheduleReadable rollingSchedule;
        private PayrollFixedScheduleReadable fixedSchedule;

        /**
         * Clear the choice selection.
         */
        public void clearChoiceListSelect(){
            choiceListSelect = -1;
        }

        /**
         * Check if RollingSchedule is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRollingSchedule(){
            return choiceListSelect == ROLLING_SCHEDULE_CHOICE;
        }

        /**
         * Get the 'rolling-schedule' element value.
         *
         * @return value
         */
        public PayrollRollingScheduleReadable getRollingSchedule(){
            return rollingSchedule;
        }

        /**
         * Set the 'rolling-schedule' element value.
         *
         * @param rollingSchedule
         */
        public void setRollingSchedule(
                PayrollRollingScheduleReadable rollingSchedule
        ){
            setChoiceListSelect(ROLLING_SCHEDULE_CHOICE);
            this.rollingSchedule = rollingSchedule;
        }

        private void setChoiceListSelect(int choice){
            if(choiceListSelect == -1){
                choiceListSelect = choice;
            }
            else if(choiceListSelect != choice){
                throw new IllegalStateException(
                        "Need to call clearChoiceListSelect() before changing existing choice");
            }
        }

        /**
         * Check if FixedSchedule is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifFixedSchedule(){
            return choiceListSelect == FIXED_SCHEDULE_CHOICE;
        }

        /**
         * Get the 'fixed-schedule' element value.
         *
         * @return value
         */
        public PayrollFixedScheduleReadable getFixedSchedule(){
            return fixedSchedule;
        }

        /**
         * Set the 'fixed-schedule' element value.
         *
         * @param fixedSchedule
         */
        public void setFixedSchedule(PayrollFixedScheduleReadable fixedSchedule){
            setChoiceListSelect(FIXED_SCHEDULE_CHOICE);
            this.fixedSchedule = fixedSchedule;
        }
    }
}
