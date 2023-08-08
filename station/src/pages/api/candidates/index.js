import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      listAllCandidates(req, res);
      break;
    default:
      break;
  }
}

async function listAllCandidates(req, res) {
  try {
    const response = await http(req, res).get("/candidates");
    console.log(response);
    res.status(response.status).json(response.data);
  } catch (error) {
    res.status(400).json({});
  }
}
