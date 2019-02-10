export default function chef(state = [], action){
    switch(action.type){
        case 'ADD_ALL':{
            state = action.payload;
            break;
        }
        case 'ADD':{
            return[
                ...state,
                action.payload
            ]
        }
        default: break;
    }
    return state;
}
