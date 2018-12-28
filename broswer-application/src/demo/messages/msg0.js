import { RA_MSG0 } from "../metadata/messageTypes";


const getMsg0 = () => {
  return {
    type: RA_MSG0,
    epdi: 0,
    status: 1
  };
}


export default getMsg0;
