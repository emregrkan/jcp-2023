import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      findJobDetailsById(req, res);
      break;
    case "POST":
      postJobDetailsById(req, res);
      break;
    default:
      break;
  }
}

async function findJobDetailsById(req, res) {
  try {
    const response = await http(req, res).get(`/jobs/${req.query.id}/details`);
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}

async function postJobDetailsById(req, res) {
  try {
    const response = await http(req, res).post(
      `/jobs/${req.query.id}/details`,
      req.body
    );
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}
