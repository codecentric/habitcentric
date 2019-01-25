import Vue from "vue";

Vue.filter("displaySchedule", function(schedule) {
  const times = repetitions => {
    if (repetitions === 1) {
      return "once";
    } else if (repetitions === 2) {
      return "twice";
    } else if (repetitions === 3) {
      return "trice";
    } else {
      return `${repetitions} times`;
    }
  };

  const frequency = frequency => {
    if (frequency === "DAILY") {
      return "day";
    } else if (frequency === "WEEKLY") {
      return "week";
    } else if (frequency === "MONTHLY") {
      return "month";
    } else if (frequency === "YEARLY") {
      return "year";
    } else {
      console.log(`unexpected frequency received: '${frequency}'`);
      return frequency;
    }
  };

  return times(schedule.repetitions) + " per " + frequency(schedule.frequency);
});
